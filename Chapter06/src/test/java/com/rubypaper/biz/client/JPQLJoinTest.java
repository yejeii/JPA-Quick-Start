package com.rubypaper.biz.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class JPQLJoinTest {

	@Test
	public void joinJPQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			dataSelectWithJoinFetch(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
	/* 조인 사용하지 않고 select 
	 * 	- select 문이 총 3번 진행됨 
	 * 	- 1. s_emp 테이블
	 *	- 2. s_emp 테이블의 직원에 해당하는 부서 (2개) 조회를 위한 s_dept 테이블 */
	private void dataSelectNotUseJoin(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT e FROM Employee e";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		// 검색 결과 처리
		List<Employee> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(Employee employee : resultList) {
			System.out.println(employee.getName());
		}
		
		em.close();
		
	}
	
	/* 묵시적 조인 : JPQL 에 반드시 연관관계에 있는 객체 언급! */
	private void dataSelectImplicitJoin(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT e, e.dept FROM Employee e";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		// 검색 결과 처리
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			System.out.println(employee.getName() + "의 부서 : " + department.getName());
		}
		
		em.close();
		
	}
	
	/* 명시적 조인 : JPQL 에 반드시 연관관계에 있는 객체 언급 + INNER JOIN ! */
	private void dataSelectExplicitJoin(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT e, d FROM Employee e INNER JOIN e.dept d";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		// 검색 결과 처리
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			System.out.println(employee.getName() + "의 부서 : " + department.getName());
		}
		
		em.close();
		
	}
	
	/* 명시적 조인 : JPQL 에 반드시 연관관계에 있는 객체 언급 + LEFT OUTER JOIN */
	private void dataSelectExplicitJoinWithOuter(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT e, d FROM Employee e LEFT OUTER JOIN e.dept d";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		// 검색 결과 처리
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			if(department != null) {
				// inner join
				System.out.println(employee.getName() + "의 부서 " + department.getName());
			} else {
				System.out.println(employee.getName() + "는 대기중.....");;
			}
		}
		
		em.close();
		
	}
	
	/* 세타 조인 : JPQL 에 엔티티명 다 포함, natural join 으로!
	 * 	- 객체 사이의 연관성 없는 경우 객체가 가진 값을 기준으로 조인 처리됨
	 */
	private void dataSelectThetaJoin(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT e, d FROM Employee e , Department d WHERE e.name = d.name";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		// 검색 결과 처리
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			System.out.println(employee.getName() + "의 부서 " + department.getName());
		}
		
		em.close();
		
	}
	
	/* 조인 페치 
	 * 	- 처음부터 조인 쿼리 이용, 연관관계에 있는 객체 get 
	 * 	- Default : INNER JOIN 으로 결과 get */
	private void dataSelectWithJoinFetch(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
//		String jpql = "SELECT e FROM Employee e JOIN FETCH e.dept";
		String jpql = "SELECT e FROM Employee e LEFT JOIN FETCH e.dept";
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		// 검색 결과 처리
		List<Employee> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(Employee employee : resultList) {
			if(employee.getDept() != null) {
				System.out.println(employee.getName() + "의 부서명 : " + employee.getDept().getName());
			} else {
				System.out.println(employee.getName());
			}
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
