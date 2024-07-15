package com.rubypaper.biz.client;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee4;

public class Employee4ServiceTest {

    @Test
    public void run() {
        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		columnAnoTest(emf);
    }

    public void columnAnoTest(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

		try {
            tx.begin();    

			// 영속성 관리 엔터티 생성
			Employee4 employee = new Employee4();
			
			employee.setId(2L);
			employee.setName("홍길동2");
			employee.setMailId("hong");
			employee.setStartDate(new Date());
			employee.setTitle("대리");
			employee.setDeptName("개발부");
			employee.setSalary(2500.00);
			employee.setCommisionPct(12.50);	// OK
			// employee.setCommisionPct(77.00);	// ERROR: Check constraint violation
			
			// 영속성 관리를 위한 엔터티 등록
			em.persist(employee);

            tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}
	}

}
