package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee2;

public class EmployeeJPQLTest {

    @Test
    public void insertAndSelectByJpql() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 직원 등록
            tx.begin();

            for (int i = 1; i <= 10; i++) {
                Employee2 employee = new Employee2();
                employee.setName("직원 - " + i);
                em.persist(employee);
            }

            tx.commit();

            // 직원 목록 조회
            String jpql = "SELECT e FROM Employee2 e where e.id = 2";
            Employee2 findEmp = em.createQuery(jpql, Employee2.class).getSingleResult();
            System.out.println(findEmp);
            // List<Employee2> employeeList = em.createQuery(jpql, Employee2.class).getResultList();
            // for(Employee2 employee : employeeList) {
                // System.out.println("---> " + employee.toString());
            // }
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
