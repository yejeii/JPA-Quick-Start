package com.rubypaper.biz.persistence;

import com.rubypaper.biz.domain.Employee;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class EmployeeRepository {

    @PersistenceContext
    private EntityManager em;

    public void insertEmployee(Employee employee) {
        System.out.println("---> JPA 로 insertEmployee() 기능 처리");
        em.persist(employee);
    }

    public void updateEmployee(Employee employee) {
        System.out.println("---> JPA 로 updateEmployee() 기능 처리");
        em.merge(employee);
    }

    public void deleteEmployee(Employee employee) {
        System.out.println("---> JPA 로 deleteEmployee() 기능 처리");
        em.remove(employee);
    }

    public Employee getEmployee(Employee employee) {
        System.out.println("---> JPA 로 insertEmployee() 기능 처리");
        return em.find(Employee.class, employee.getId());
    }

    public List<Employee> getEmployeeList(Employee employee) {
        System.out.println("---> JPA 로 getEmployeeList() 기능 처리");

        // 검색 결과 >>
        // 사원 테이블 조회 후 부서 개수만큼 부서 테이블 조회
        // 즉, multi 행 조회에선 @ManyToOne(EAGER) 로 설정해도 JOIN 발생 X
        //return em.createQuery("FROM Employee emp ORDER BY emp.id DESC").getResultList();

        // 직원 정보 조회시, 관련 부서 정보까지 함께 가져오기 위해 페치 조인 사용
        return em.createQuery("FROM Employee emp JOIN FETCH emp.dept dept ORDER BY emp.id DESC").getResultList();
    }
}
