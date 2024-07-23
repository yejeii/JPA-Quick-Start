package com.rubypaper.biz.persistence;

import com.rubypaper.biz.domain.Department;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DepartmentRepository {

    @PersistenceContext
    private EntityManager em;

    public void insertDepartment(Department department) {
        System.out.println("---> JPA 로 insertDepartment() 기능 처리");
        em.persist(department);
    }

    public Department getDepartment(Department department) {
        System.out.println("---> JPA 로 getDepartment() 기능 처리");
        return em.find(Department.class, department.getDeptID());
    }
}
