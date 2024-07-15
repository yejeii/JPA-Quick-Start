package com.rubypaper.biz.client;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee3;

public class Employee3ServiceTest {

    @Test
    public void run() {
        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		withUniqueConstraintTest(emf);
    }

    /**
     * Hibernate >>
     *  alter table Employee3 
     *      add constraint UKmicai4h5vfie24lls41yox65 unique (name, mailId)
     */
    public void withUniqueConstraintTest(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

		try {
			
			// 영속성 관리 엔터티 생성
			Employee3 employee = new Employee3();
			
			employee.setId(4L);
			employee.setName("홍길동4");
			employee.setMailId("hong4");
			employee.setStartDate(new Date());
			employee.setTitle("대리");
			employee.setDeptName("개발부");
			employee.setSalary(2500.00);
			employee.setCommisionPct(12.50);
			
			tx.begin();    

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
