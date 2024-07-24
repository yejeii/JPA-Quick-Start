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

        // Many 에 해당하는 컬렉션 이용시 예외 발생
        // SELECT 절에 포함되지 않은 사원 정보까지 사용하려 했기 때문
        // @OneToMany 의 기본 fetch 속성이 LAZY 이기 때문
        // 해결: EAGER 로 변경
        return em.find(Department.class, department.getDeptID());
    }
}
