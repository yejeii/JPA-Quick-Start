package com.rubypaper.biz.client;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee1;

/**
 * JPA 에서 관리한 Entity 생성
 * 	- Entity 를 이용해 데이터 삽입하는 테스트
 * 	  1. EntityManagerFactory 이용해서 EntityManager 객체 생성
 * 	  2. EntityManager 를 이용해 영속성 관리
 * 		 persist()
 */
public class Employee1ServiceTest {

    @Test
    public void run() {
        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		
		// nonTransactionInsert(emf);
		withTableNameInsert(emf);
    }

	/** 트랜잭션 없이 엔티티 객체 생성, 영속성 컨텍스트에 저장
	 * 	-> Employee1 에 대한 테이블은 생성되었으나, DB 반영 X
	 */
    public void nonTransactionInsert(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();

        try {
			// 영속성 관리 엔터티 생성
			Employee1 employee = new Employee1();
			
			employee.setId(1L);
			employee.setName("홍길동");
			employee.setMailId("hong");
			employee.setStartDate(new Date());
			employee.setTitle("대리");
			employee.setDeptName("개발부");
			employee.setSalary(2500.00);
			employee.setCommisionPct(12.50);
			
			// 영속성 관리를 위한 엔터티 등록
			em.persist(employee);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}
    }

	/** 트랜잭션 사이에서 엔티티 객체 생성, 영속성 컨텍스트에 저장 
	 * 	-> Employee1 에 대한 테이블 생성 & DB 반영
	 */
	public void withTableNameInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// 엔터티 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();

		try {
			// 영속성 관리 엔터티 생성
			Employee1 employee = new Employee1();
			
			employee.setId(1L);
			employee.setName("홍길동");
			employee.setMailId("hong");
			employee.setStartDate(new Date());
			employee.setTitle("대리");
			employee.setDeptName("개발부");
			employee.setSalary(2500.00);
			employee.setCommisionPct(12.50);

			// 트랜잭션 시작 
			tx.begin();
			
			// 영속성 관리를 위한 엔터티 등록
			em.persist(employee);

			// 트랜잭션 종료
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}
	}
}

