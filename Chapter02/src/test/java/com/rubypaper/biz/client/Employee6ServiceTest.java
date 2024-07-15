package com.rubypaper.biz.client;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee6;

public class Employee6ServiceTest {

    @Test
    public void run() {
        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		temporalAnoTest(emf);
    }

    public void temporalAnoTest(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

		try {
            tx.begin();    

			// 영속성 관리 엔터티 생성
			Employee6 employee = new Employee6();
			
			employee.setId(2L);
			employee.setName("홍길동2");
			employee.setMailId("hong2");
			employee.setStartDate(new Date());
			employee.setTitle("대리");
			employee.setDeptName("개발부");
			employee.setSalary(2500.00);
			employee.setCommisionPct(12.50);

            // 테이블 칼럼 생성 X -> 영속성 컨텍스트의 엔티티에만 저장
            employee.setSearchKeyword("홍");
            employee.setSearchCondition("성");
			
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
