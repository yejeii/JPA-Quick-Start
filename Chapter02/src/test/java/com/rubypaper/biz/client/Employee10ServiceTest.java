package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee10;

public class Employee10ServiceTest {

    @Test
    public void run() {

        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		generatedValueTableAnoTest(emf);
		emf.close();
    }

    public void generatedValueTableAnoTest(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

        try {

			tx.begin();
			
			// 회원 등록 요청
			Employee10 employee = new Employee10();
			employee.setName("둘리");	
			employee.setMailId("dulli");

			System.out.println("등록 전 id - " + employee.getId()); // null

            // 영속성 관리를 위한 엔터티 등록
            em.persist(employee);
            
            System.out.println("등록 후 id - " + employee.getId());
			
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}

    }
}

