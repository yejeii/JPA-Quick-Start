package com.rubypaper.biz.client;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class JPQLNamedQueryClient {

    @Test
	public void NamedQueryJPQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			dataSelectByNativeQuery(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}	
   
	private void dataSelectByNamedQuery(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		TypedQuery<Employee> query = em.createNamedQuery("Employee.searchByName", Employee.class);
		query.setParameter("searchKeyword", "%영업%");
		
		// 검색 결과 처리
		List<Employee> resultList = query.getResultList();
		for(Employee result : resultList) {
			System.out.println("---> " + result.getId() + "번 직원 이름 : " + result.getName());
		}
		
		em.close();
		
	}

	private void dataSelectByNativeQuery(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		Query query = em.createNamedQuery("Employee.searchByDeptId");
		query.setParameter("deptId", 2L);
		query.setFirstResult(0);
		query.setMaxResults(3);

		List<Object[]> resultList = query.getResultList();
		for (Object[] result: resultList) {
			System.out.println("---> " + Arrays.toString(result));
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
