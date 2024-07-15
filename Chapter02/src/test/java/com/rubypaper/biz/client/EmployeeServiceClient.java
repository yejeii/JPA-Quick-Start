package com.rubypaper.biz.client;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee10;
import com.rubypaper.biz.domain.EmployeeId;


public class EmployeeServiceClient {

	@Test
	public void run() {

		// <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		// nonTransactionInsert(emf);
	}

	public void withTransactionInsert(EntityManagerFactory emf) {

		// 엔티티 매니저 생성
		EntityManager em = emf.createEntityManager();
		
		// 엔티티 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();
		
		try {
			// 트랜잭션 시작
			tx.begin();		

			// 직원 엔티티 생성 및 초기화 - 비영속 상태
			EmployeeId empId = new EmployeeId(2L, "guest456");
			
			Employee10 employee = new Employee10();
			employee.setEmpId(empId);
//			employee.setId(1L);
			employee.setName("둘리");
//			employee.setMailId("gurum");
			employee.setStartDate(new Date());
			employee.setTitle("과장");
			employee.setDeptName("총무부");
			employee.setSalary(2900.00);
			employee.setCommissionPct(12.50);

			System.out.println("등록 전 id : " + employee.getEmpId());

			// 직원 등록 처리 - 영속 상태로 변경
			em.persist(employee);	
			System.out.println("등록 후 id : " + employee.getEmpId());
			
			// 트랜잭션 종료(COMMIT)
			tx.commit();	
			
			// 등록한 회원 검색
			Employee10 findEmployee = em.find(Employee10.class, empId);
			System.out.println("검색한 회원 정보");
			System.out.println(findEmployee.toString());
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
