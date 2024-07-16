package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee12;
import com.rubypaper.biz.domain.Employee12Id;

public class Employee12ServiceTest {

    @Test
    public void run() {

        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		dupliKeyTest(emf);
        findEmployee(emf);

		emf.close();
    }

    public void dupliKeyTest(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

        try {

			tx.begin();
			
            // 복합키 객체 생성
            Employee12Id empId = new Employee12Id(2L, "hone2");

			// 사원 객체 생성
			Employee12 employee = new Employee12();
            employee.setEmpId(empId);   // 사원 객체의 식별자 멤버 초기화
			employee.setName("생치");	
            
			System.out.println("등록 전 id - " + employee.getEmpId()); // Employee12Id(id=1, mailId=gugu123)

            // 영속성 관리를 위한 엔터티 등록
            em.persist(employee);
            
            System.out.println("등록 후 id - " + employee.getEmpId());  // Employee12Id(id=1, mailId=gugu123)

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}

    }

    public void findEmployee(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();

        Employee12Id searchEmpId = new Employee12Id(1L, "gugu123");
        Employee12 findEmp = em.find(Employee12.class, searchEmpId);
        System.out.println("검색된 사원 정보 : " + findEmp.toString());
    }
}
