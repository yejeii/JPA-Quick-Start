package com.rubypaper.biz.client;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee2;

public class Employee2ServiceTest {

    @Test
    public void run() {
        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		
		withTableNameInsert(emf);
    }

	/** @Table(name="Employee2Test") 로 테이블 생성 */
    public void withTableNameInsert(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
		
		// 엔터티 트랜잭션 생성
		EntityTransaction tx = em.getTransaction();

        try {
			// 영속성 관리 엔터티 생성
			Employee2 employee = new Employee2();
			
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

