package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee1;

import java.util.Optional;

public class Employee1ServiceClient {

	@Test
    public void run() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
		try {
			deleteAndUpdate(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

	/**
	 * 비영속 상태
	 */
	@Test
	public void nonPersist() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
		EntityManager em = emf.createEntityManager();

		try {
			// Entity 만 생성된 상태
			// 영속성 컨테이너에 등록이 되지 않은 상태
			Employee1 employee = new Employee1();
			employee.setName("홍홍이");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}
		
	}

	/**
	 * 1. 영속성 컨테이너에 등록 후 DB 저장 
	 * 	  -> 반드시 트랜잭션 내에서 persist() 호출되어야 함
	 * 
	 * 2. 영속성 컨테이너에 있는 엔티티 수정
	 * 	  -> DB 에 반영되려면 트랜잭션 내에서 수정되어야 함!
	 */
	@Test
	public void persistAndSetEntity() {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			// Entity 만 생성된 상태
			// 영속성 컨테이너에 등록이 되지 않은 상태
			Employee1 employee = new Employee1();
			employee.setName("첫 이름");

			// 트랜잭션 시작
			tx.begin();

			// 영속성 관리를 위한 엔티티 등록
			em.persist(employee);

			// 트랜잭션 종료
			tx.commit();

			/*
			* Entity 수정
			* 결과적으로 UPDATE 문장이 DB 에 전송
			* 
			* Dirty Check
			* 영속성 컨테이너는 관리중인 엔터티의 변경이 되는 순간, 변경을 감지하여 데이터베이스에 update 문장을 전송.
			*/
			tx.begin();
			employee.setName("수정 1");
			tx.commit();

			// DB 변경 사항을 반영하려면, 반드시 트랜잭션 내부에서 수행해야 함!
			// employee.setName("수정 2");

			// *********** 영속성 컨테이너 등록, Entity Manager 의 find() **** //
			Employee1 findEmp = em.find(Employee1.class, 1L);
			System.out.println(findEmp.toString());

			/*
			* 1. find() 를 사용한다고 하여 항상 DB 를 조회하지 X
			* 	  영속성 컨텍스트에서 우선 조회 후, 없으면 DB 에서 조회
			* 2. 조회된 사원 정보의 이름과 DB 에 저장된 사원 정보 이름이 서로 다름.
			* 	  이는 즉, Entity 의 수정 정보가 DB 에 반영이 안되었다는 뜻.
			*    -> 영속 상태 정보와 DB 정보를 맞춰주기 위한 동기화 과정이 필요! 
			* 	  -> flush() 필요!
			*    -> 트랜잭션이 커밋될 때 자동으로 flush() 호출됨
			* 	  -> Entity 상태 정보와 DB 정보를 맞출 수 있는 것 :) 
			*/
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
			emf.close();
		}
	}

	/**
	 * 영속성 컨테이너에 없는 엔티티 조회
	 * 
	 * find() == "DB 의 데이터를 조회하겠다" 
	 * -> 영속성 컨텍스트 내에서 검색 -> SELECT SQL문 생성 및 전송 
	 * -> DB 에서 조회, 반환 -> 영속성 컨텍스트에 엔티티 생성 및 등록 -> 애플리케이션 쪽에 조회된 엔티티 전송
	 * 
	 * Hibernate >>
	 * 	영속성 컨텍스트에 없어서 select 전송 -> DB 에서 조회 후 반환 -> 영속성 컨텍스트에 저장
	 * 	-> 트랜잭션 내에서 수정 발생 -> UPDATE 문 저장 -> commit 시 전송, 영구 저장
	 * 
	 * 실습시 주의사항 
	 * persistence.xml - DDL "update" 로 진행!
	 */
	@Test
	public void findAndUpdateEntity() {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		Employee1 findEmp = em.find(Employee1.class, 1L);
		System.out.println("수정 전 이름 : " + findEmp.toString());

		findEmp.setName("징징이");

		tx.commit();

	}

	/** 영속성 컨텍스트에서 분리
	 * 
	 * 실습 >>
	 * persistence.xml - DDL "create" 로 진행!
	 */
	@Test
	public void detachAndUpdate() {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		try {

			Employee1 employee = new Employee1();
			employee.setName("첫 이름");
	
			tx.begin();
			em.persist(employee);
			tx.commit();
	
			// ***** 엔티티 분리 실습 ****** //
			if(em.contains(employee)) {
				System.out.println("영속성 컨텍스트 등록된 상태");
			}
	
			// 엔티티 분리
			em.detach(employee);
	
			if(!em.contains(employee)) {
				System.out.println("영속성 컨텍스트에서 분리된 상태");
			}
	
			// 분리된 엔티티 수정
			employee.setName("이름 수정");
	
			// 분리 상태의 엔티티는 수정을 하더라도 영속성 컨텍스트에 반영 X
			
			// 분리된 엔티티 조회
			// 분리 상태의 엔티티는 영속성 컨텍스트에 존재하지 않으므로 
			// find() 로 조회하면 select 문장이 생성되어 DB 에서 찾아와 영속성 컨텍스트에 새롭게 등록된다.
			Employee1 findEmp = em.find(Employee1.class, 1L);
			System.out.println("분리된 엔티티의 수정 상태 : " + findEmp.toString());
			// Employee1(id=1, name=첫 이름, startDate=null, title=null, deptName=null, salary=null, commissionPct=null)

			// employee 객체와 findEmp 는 서로 다른 객체~!

			// merge
			tx.begin();
			em.merge(employee);
			tx.commit();

			Employee1 findEmp2 = em.find(Employee1.class, 1L);
			System.out.println("분리된 엔티티의 결합 상태 : " + findEmp2.toString());
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
			emf.close();
		}
	}

	/** merge() :영속성 컨텍스트에서 분리 -> 관리 상태로 전환 실습
	 * 
	 * merge() 필요한 이유  
	 * DB 와의 동기화 상태가 아닌 엔티티(준영속 상태)를 다시 DB 에 저장하기 위함
	 * 
	 * merge() 동작 방식
	 *	1차 캐시에서 해당 식별자 존재 검사
	 * 	-> 없다? DB 에서 조회(SELECT 문 전송), DB에 데이터가 있으면 객체 등록
	 * 	
	 *  Entity 를 수정한 경우, 수정한 결과가 반영되지 않음
	 *  DB 에서 조회된 내용으로 덮어쓰기됨
	 * 
	 * 테스트
	 * 영속 -> 준영속(영속성 컨테이너 close) -> 영속(merge)
	 * 
	 * 이렇게 진행되는 변화에 대한 과정을 콘솔 로그로 보고 이해를 해야함.
	 * 
	 * mergeEmployee() 호출 : SELECT -> UPDATE
	 * 
	 * 	1. merge() 호출
	 *     준영속 상태의 변경이 발생한 엔티티를 매개변수로 전달
	 * 
	 *	2. 새롭게 생성된 영속성 컨텍스트에는 기존 엔티티가 없는 상태
	 *     매개변수로 전달된 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티 검색 수행
	 * 	   
	 *     만약 1차 캐시에서 찾지 못하면 DB 에 가서 검색 수행 -> SELECT 문장이 작성되고 전송됨  : find()
	 *  
	 *     DB 에서 검색된 결과를 바탕으로 엔티티를 생성, 1차 캐시에 저장함
	 * 
	 * 	3. 1차 캐시에 저장된 영속 엔티티에 매개변수로 전달된 엔티티의 값(변경이 발생)으로 반영
	 * 
	 * 	   현재 영속 엔티티의 name 값은 "첫 이름" 이고,
	 * 	   준영속 상태의 엔티티 name 은 "수정 이름" 인 상태..
	 *     따라서, "첫 이름" 을 "수정 이름" 으로 수정해야 함.. -> UPDATE 문장이 작성되고 전송됨 : 변경 감지
	 * 
	 * 	4. 모든 변경사항이 반영되어 동기가 이루어진 상태
	 * 	   최종적으로 수정사항이 모두 반영된 영속 엔티티 반환
	 * 
	 * 	5. 결론은 현재 두 가지 상태의 엔티티가 공존하므로,
	 * 	   반드시 엔티티 데이터의 영속 및 동기화를 위해 영속 상태의 엔티티(merge() 를 반환하는 엔티티)를 사용해야 함!!
	 */
	@Test
	public void detachAndPersist() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");

		// 준영속 상태의 엔티티 반환
		Employee1 employee = createEmployee("첫 이름", emf);

		// 준영속 상태에서 변경이 발생
		employee.setName("이름 수정"); 

		// 준영속 상태의 엔티티를 영속상태로 변경
		mergeEmployee(employee, emf);

	}

	public Employee1 createEmployee(String name, EntityManagerFactory emf) {
		System.out.println("=============== createEmployee() start ============= ");

		// 첫 번째 영속성 컨테이너 시작
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		// 트랜잭션 시작
		tx.begin();

		// 엔티티 생성 및 등록
		Employee1 employee = new Employee1();
		employee.setName(name);

		em.persist(employee);

		// 트랜잭션 종료
		tx.commit();

		// 첫 번째 영속성 컨테이너 종료
		// 영속성 컨테이너에 있는 엔티티를 분리한 후 컨테이너 종료됨
		em.close();

		System.out.println("=============== createEmployee() end ============= ");
		
		// 준영속 상태의 엔티티 반환
		return employee;
	}

	public void mergeEmployee(Employee1 employee, EntityManagerFactory emf) {

		System.out.println("=============== mergeEmployee() start ============= ");

		// 두 번째 영속성 컨텍스트 시작
		EntityManager em = emf.createEntityManager();
		
		// 병합 : 준영속 상태의 employee 엔티티를 영속 상태로 변경
		// 준영속 상태의 엔티티가 변경됨.. -> 수정한 내용을 DB 에 반영해야 함!
		// -> 새로운 트랜잭션을 시작해야 함
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		// 병합 
		// 병합이 정상적으로 완료되면, 영속 상태의 엔티티가 반환됨
		// 두 가지 상태의 엔티티가 공존하게 됨.
		// 	- 1. 준영속 상태의 엔티티 : 파라미터로 들어온 Employee 객체
		// 	- 2. 영속 상태의 엔티티 : merge() 호출로 반환된 객체
		Employee1 mergedEmp = em.merge(employee);
		
		// 트랜잭션 종료
		// flush 하여 변경사항을 SQL 로 만들어 DB 에 전송
		tx.commit();

		// 두 가지 상태의 엔티티 비교
		if(mergedEmp != employee) {
			System.out.println("두 객체는 동일하지 않다!");
		}
		
		System.out.println("employee(준영속 엔티티) : " + employee.toString());
		System.out.println("mergeEmp(영속 엔티티) : " + mergedEmp.toString());

		System.out.println("employee 상태 : " + em.contains(employee));
		System.out.println("mergeEmp(영속 엔티티) : " + em.contains(mergedEmp));

		System.out.println("=============== mergeEmployee() end ============= ");
	}

	/**
	 * 영속성 컨텍스트에서 삭제
	 * 	-> 트랜잭션 안에서 실행해야 DB 까지 delete 반영됨!
	 */
	public void deleteAndUpdate(EntityManagerFactory emf) {

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		Employee1 employee = new Employee1();
		employee.setName("첫 이름");

		tx.begin();
		em.persist(employee);
		tx.commit();

		// 영속성 컨텍스트에서 바로 찾아오겠지
		Employee1 findEmp = em.find(Employee1.class, 1L);
		System.out.println("엔티티의 상태 : " + findEmp.toString());

		// ***** 엔티티 삭제 실습 ****** //
		tx.begin();
		em.remove(findEmp);
		tx.commit();

		// 삭제 상태의 엔티티 확인 : 둘다 같은 주소 참조 -> remove 됨!
		if(employee.equals(findEmp)) {
			System.out.println("동일한 객체 참조!");
		}
		if(em.contains(employee)) {
			System.out.println("employee 엔티티 관리중!");
		}
		if(em.contains(findEmp)) {
			System.out.println("findEmp 엔티티 관리중!");
		}

		// 메모리상에서만 존재
		System.out.println("findEmp 상태 : " + Optional.ofNullable(findEmp));

		// 다시 관리 상태로 변경
		tx.begin();
		em.merge(findEmp);
		// em.persist(findEmp); : Error 발생
		tx.commit();
	}

}
