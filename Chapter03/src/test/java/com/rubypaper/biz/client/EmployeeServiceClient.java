package com.rubypaper.biz.client;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

import org.hibernate.resource.transaction.internal.SynchronizationRegistryStandardImpl;

import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.domain.EmployeeId;

public class EmployeeServiceClient {

	public static void main(String[] args) {

		// 엔티티 매니저 팩토리 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
		
		// 엔티티 매니저 생성
		EntityManager em = emf.createEntityManager();
		
		// 플러시 모드 설정
//		em.setFlushMode(FlushModeType.COMMIT);	// 트랜잭션 commit 시에만 플러시 동작하도록 설정
		
		// 엔티티 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		
		try {

			// 직원 엔티티 등록
//			Employee employee = new Employee();
//			employee.setId(1L);
//			employee.setName("둘리");
//			employee.setMailId("gurum");
//			employee.setStartDate(new Date());
//			employee.setTitle("과장");
//			employee.setDeptName("총무부");
//			employee.setSalary(2900.00);
//			employee.setCommissionPct(12.50);
			
			// 직원 엔티티 등록
			tx.begin();
			for (int i = 1; i <= 10; i++) {
				Employee employee = new Employee();
				employee.setName("둘리"+i);
				employee.setStartDate(new Date());
				employee.setTitle("과장");
				employee.setDeptName("총무부");
				employee.setSalary(2900.00);
				employee.setCommissionPct(12.50);
				em.persist(employee);
			}
			tx.commit();
			
			// 직원 검색
//			Employee findEmp = em.find(Employee.class, 1L);
//			Employee findEmp = em.getReference(Employee.class, 1L);
//			System.out.println("검색된 직원 이름 : " + findEmp.getName());
			
			// 직원 목록 검색
			String jpql = "SELECT e FROM Employee e ORDER BY e.id DESC";
			List<Employee> employeeList = 
						em.createQuery(jpql, Employee.class)
							.getResultList();
			for(Employee employee : employeeList) {
				System.out.println("---> " + employee.toString());
			}
			
			// 트랜잭션 시작
//			tx.begin();	
			
			// 직원 등록 처리 -> 엔티티, 영속 컨텍스트에서 관리 상태로 전환
//			em.persist(employee);	
//			em.merge(employee);
			
//			if(em.contains(employee)) {
//				System.out.println(employee.toString() + " MANAGED");
//			}
			
			// 트랜잭션 종료(COMMIT)
//			tx.commit();
			
//			for (int i = 0; i < 30; i++) {
//				Thread.sleep(1000);	// 30초
//				System.out.println("다른 사용자가 데이터 수정중 ... " + i);
//			}
			
			// 엔티티 refresh(테이블 변화를 엔티티에 반영)
//			em.refresh(employee);
//			System.out.println("갱신된 직원 정보 : " + employee.toString());
			
			// 모든 엔티티를 분리 상태로 전환
//			em.clear();
			
			// 1번 직원 엔티티를 분리 상태로 전환
//			em.detach(employee);
			
//			if(!em.contains(employee)) {
//				System.out.println(employee.toString() + " DETACHED");
//			}
			
			// 직원 이름 수정
//			tx.begin();
//			employee.setName("똘리");
			
			// 분리 상태의 엔티티, 관리 상태로 전환
//			Employee mergedEmp = em.merge(employee);
//			tx.commit();
			
			// 관리 상태 여부 확인
//			System.out.println("employee 관리 : " + em.contains(employee));
//			System.out.println("mergedEmp 관리 : " + em.contains(mergedEmp));
			
//			findEmp.setName("둘리");	

			// 엔티티 삭제
//			em.remove(employee);
			
			// 직원 검색
//			Employee findEmp1 = em.find(Employee.class, 1L);
//			Employee findEmp2 = em.find(Employee.class, 1L);
			
			// 객체 동일성 비교
//			if(findEmp1.equals(findEmp2)) {
//				System.out.println("검색된 두 객체는 동일한 객체");
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// 트랜잭션 종료(ROLLBACK)
			tx.rollback();
		} finally {
			// 엔티티 매니저 및 엔티티 매니저 팩토리 종료
			em.close();
			emf.close();
		}
	}

}
