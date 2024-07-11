package com.rubypaper.biz.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class JPQLSubquery {

    @Test
    public void executeJPQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			dataSelectUseSubquery3(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

    /* SubQuery : 직원 수가 3 이상인 부서 객체 조회 */
    private void dataSelectUseSubquery(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT d FROM Department d "
                    + "WHERE ( SELECT COUNT(e) "
                    + "        FROM Employee e "
                    + "        WHERE d.id = e.dept "               
                    + "      ) >= 3";
		TypedQuery<Department> query = em.createQuery(jpql, Department.class);
		
		// 검색 결과 처리
		List<Department> resultList = query.getResultList();
		System.out.println("소속된 직원이 3명 이상인 부서 목록");
		for(Department department : resultList) {
			System.out.println(department.getName());
		}
		
		em.close();
	}

    /* SubQuery : 전 직원의 평균 급여보다 높은 급여를 받는 직원 객체 조회 */
    private void dataSelectUseSubquery2(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT e FROM Employee e JOIN FETCH e.dept "
                    + "WHERE e.salary > ( SELECT AVG(e.salary) "
                    + "                   FROM Employee e "
                    + "                  )";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		// 검색 결과 처리
		List<Employee> resultList = query.getResultList();
		System.out.println("전 직원의 평균 급여보다 높은 급여를 받는 직원 목록");
		for(Employee employee : resultList) {
			System.out.println(employee.getName());
		}
		
		em.close();
	}

    /* SubQuery : 특정 부서에 속하지 않은 직원 목록 출력 */
    private void dataSelectUseSubquery3(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT e FROM Employee e "
                    + "WHERE NOT EXISTS ( SELECT d "
                    + "                   FROM Department d "
                    + "                   WHERE d = e.dept )";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		// 검색 결과 처리
		List<Employee> resultList = query.getResultList();
		System.out.println("부서에 속하지 않는 직원 명단");
		for(Employee employee : resultList) {
			System.out.println(employee.getName());
		}
		
		em.close();
	}


    private void dataInsert(EntityManagerFactory emf) {

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Department department1 = new Department();
		department1.setName("개발부");
		
		for(int i=1; i<=3; i++) {
			Employee employee = new Employee();
			employee.setName("개발세발직원" + i);
			employee.setMailId("Dev-" + i);
			employee.setSalary(12700.00 * i);
			employee.setDept(department1);
		}
		em.persist(department1);
		
		Department department2 = new Department();
		department2.setName("영업부");
		
		for(int i=1; i<=3; i++) {
			Employee employee = new Employee();
			employee.setName("영업직원" + i);
			employee.setMailId("Sale-" + i);
			employee.setSalary(27300.00 * i);
			employee.setDept(department2);
		}
		em.persist(department2);
		
		Department department3 = new Department();
		department3.setName("인재개발부");
		em.persist(department3);
		
		// Outer Join 을 위한 추가 : 부서 정보가 없는 새로운 직원 추가
		Employee employee = new Employee();
		employee.setName("아르바이트");
		employee.setMailId("Alba-01");
		employee.setSalary(10000.00);
		em.persist(employee);
		
		// Theta Join 을 위한 추가 : 이름이 영업부인 새로운 직원 추가
		Employee employee2 = new Employee();
		employee2.setName("영업부");
		employee2.setSalary(11850.00);
		em.persist(employee2);
		
		em.getTransaction().commit();
		em.close();
	}
}
