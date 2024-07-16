package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee;

public class EmployeeServiceClient {

	@Test
    public void run() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
		try {
			detachAndPersist(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

	/**
	 * 비영속 상태
	 */
	public void nonPersist(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// Entity 만 생성된 상태
		// 영속성 컨테이너에 등록이 되지 않은 상태
		Employee employee = new Employee();
		employee.setName("홍홍이");
	}

	/**
	 * 1. 영속성 컨테이너에 등록 후 DB 저장 
	 * 	  -> 반드시 트랜잭션 내에서 persist() 호출되어야 함
	 * 
	 * 2. 영속성 컨테이너에 있는 엔티티 수정
	 * 	  -> DB 에 반영되려면 트랜잭션 내에서 수정되어야 함!
	 */
	public void persistAndSetEntity(EntityManagerFactory emf) {

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		// Entity 만 생성된 상태
		// 영속성 컨테이너에 등록이 되지 않은 상태
		Employee employee = new Employee();
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
		 * 영속성 컨테이너는 관리중인 엔터티의 변경이 되는 순간, 변경을
		 * 감지하여 데이터베이스에 update 문장을 전송.
		 * 
		 */
		tx.begin();
		employee.setName("수정 1");
		tx.commit();

		// DB 변경 사항을 반영하려면, 반드시 트랜잭션 내부에서 수행해야 함!
		employee.setName("수정 2");

		// *********** 영속성 컨테이너 등록, Entity Manager 의 find() **** //
		Employee findEmp = em.find(Employee.class, 1L);
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
	}

	/**
	 * 영속성 컨테이너에 없는 엔티티 조회
	 * 
	 * find() -> 영속성 컨텍스트 내에서 검색 -> SELECT SQL문 생성 및 전송 
	 * -> DB 에서 조회, 반환 -> 영속성 컨텍스트에 엔티티 생성 및 등록 -> 애플리케이션 쪽에 조회된 엔티티 전송
	 * 
	 * Hibernate >>
	 * 	영속성 컨텍스트에 없어서 select 전송 -> DB 에서 조회 후 반환 -> 영속성 컨텍스트에 저장
	 * 	-> 트랜잭션 내에서 수정 발생 -> UPDATE 문 저장 -> commit 시 전송, 영구 저장
	 * 
	 * 실습시 주의사항 
	 * persistence.xml - DDL "update" 로 진행!
	 */
	public void findAndUpdateEntity(EntityManagerFactory emf) {

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		Employee findEmp = em.find(Employee.class, 1L);
		System.out.println("수정 전 이름 : " + findEmp.toString());

		findEmp.setName("징징이");

		tx.commit();

	}

	/**
	 * 영속성 컨텍스트에서 분리
	 * 
	 * 실습시 주의사항 
	 * persistence.xml - DDL "create" 로 진행!
	 */
	public void detachAndUpdate(EntityManagerFactory emf) {

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		Employee employee = new Employee();
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
		Employee findEmp = em.find(Employee.class, 1L);
		System.out.println("분리된 엔티티의 수정 상태 : " + findEmp.toString());
		// Employee1(id=1, name=첫 이름, startDate=null, title=null, deptName=null, salary=null, commissionPct=null)
	}

	/**
	 * 영속성 컨텍스트에서 분리 -> 관리 상태로 전환 실습
	 */
	public void detachAndPersist(EntityManagerFactory emf) {
		
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		Employee employee = new Employee();
		employee.setName("첫 이름");

		tx.begin();
		em.persist(employee);
		tx.commit();

		// 엔티티 분리
		em.detach(employee);

		// ***** 엔티티 분리 -> 관리 실습 ***** //
		em.merge(employee);

		if(em.contains(employee)) {
			System.out.println("영속성 컨텍스트 등록된 상태");
		}

		/*
		 * merge() 동작 방식
		 *	1차 캐시에서 해당 식별자 존재 검사
		 * 	-> 없다? DB 에서 조회(SELECT 문 전송), DB에 데이터가 있으면 객체 등록
		 * 	
		 *  Entity 를 수정한 경우, 수정한 결과가 반영되지 않음
		 *  DB 에서 조회된 내용으로 덮어쓰기됨
		 */
	}


	/**
	 * 영속성 컨텍스트에서 삭제
	 * 	-> 트랜잭션 안에서 실행해야 DB 까지 delete 반영됨!
	 */
	public void deleteAndUpdate(EntityManagerFactory emf) {

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		Employee employee = new Employee();
		employee.setName("첫 이름");

		tx.begin();
		em.persist(employee);
		tx.commit();

		// 영속성 컨텍스트에서 바로 찾아오겠지
		Employee findEmp = em.find(Employee.class, 1L);
		System.out.println("엔티티의 상태 : " + findEmp.toString());

		// ***** 엔티티 삭제 실습 ****** //
		tx.begin();
		em.remove(findEmp);
		tx.commit();
	}

}
