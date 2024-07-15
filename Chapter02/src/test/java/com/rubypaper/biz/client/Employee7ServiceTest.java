package com.rubypaper.biz.client;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee7;

public class Employee7ServiceTest {

    @Test
    public void run() {
        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		
        try {
			allArgsAnoTest(emf);
			findEmployee(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    public void allArgsAnoTest(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();    
        
        // 영속성 관리 엔터티 생성

        // lomnok 의 @Data 삭제 -> setter 사용 불가
        // lombok 에서 만들어주는 생성자를 사용해야 함

        // 주의!!
        // searchCondition, searchKeyword 는 JPA @Transient 대상이지만
        // 생성자는 lombok 이 만든 것으로 JPA 어노테이션과 호환성 X
        // -> 빠짐없이 추가해야 함.
        Employee7 employee = new Employee7(1L, 
                                        "홍길동", 
                                        "hong", 
                                        new Date(), 
                                        "대리", 
                                        "개발부", 
                                        2500.00, 
                                        12.50, 
                                        "성", 
                                        "홍");
        
        /*
        Employee7 employee = new Employee7();
        
        employee.setId(1L);
        employee.setName("홍길동");
        employee.setMailId("hong");
        employee.setStartDate(new Date());
        employee.setTitle("대리");
        employee.setDeptName("개발부");
        employee.setSalary(2500.00);
        employee.setCommisionPct(12.50);
        */
        
        // 영속성 관리를 위한 엔터티 등록
        em.persist(employee);

        tx.commit();
        em.close();
	}

    public void findEmployee(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();

        Employee7 findEmployee = em.find(Employee7.class, 1L);
        System.out.println("검색한 사원 정보");
        System.out.println(findEmployee.toString());
        // @ToString
        // Employee7(id=1, name=홍길동, mailId=hong, startDate=2024-07-15, title=대리, deptName=개발부, salary=2500.0, commisionPct=12.5, searchCondition=null, searchKeyword=null)

        // @ToString(exclude={"searchCondition", "searchKeyword"})
        // Employee7(id=1, name=홍길동, mailId=hong, startDate=2024-07-15, title=대리, deptName=개발부, salary=2500.0, commisionPct=12.5)
    }
}
