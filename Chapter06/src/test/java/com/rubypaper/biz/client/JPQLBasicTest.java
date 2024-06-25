package com.rubypaper.biz.client;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.domain.EmployeeSalaryData;

public class JPQLBasicTest {

	@Test
	public void basicJPQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
//			dataSelectWithTypedQuery(emf);
			dataSelectWithQuery(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
	/* 
	 * SELECT * : TypedQuery
	 */
	private void dataSelectWithTypedQuery(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT e FROM Employee AS e";
		
		// JPQL 전송
		TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		// 검색 결과 처리
		List<Employee> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(Employee result : resultList) {
			System.out.println("---> " + result.toString());
		}
		
		em.close();
		
	}
	
	/* 
	 * 선택적 검색 : Query
	 */
	private void dataSelectWithQuery(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT id, name, deptName, salary FROM Employee";
		
		// JPQL 전송
		Query query = em.createQuery(jpql);
		
		// 검색 결과 처리
		List<Object[]> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(Object[] result : resultList) {
			System.out.println("---> " + Arrays.toString(result));
		}
		
		em.close();
		
	}
	
	/* 
	 * 선택적 검색 : TypedQuery + New 특정 객체
	 */
	private void dataSelectWithTypedQueryWithNew(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT " 
					+ "New com.rubypaper.biz.domain.EmployeeSalaryData(id, salary, commissionPct)"
					+ "FROM Employee";
		
		// JPQL 전송
		TypedQuery<EmployeeSalaryData> query = em.createQuery(jpql, EmployeeSalaryData.class);
		
		// 검색 결과 처리
		List<EmployeeSalaryData> resultList = query.getResultList();
		System.out.println("검색된 직원 목록");
		for(EmployeeSalaryData result : resultList) {
			System.out.println("---> " + result.toString());
		}
		
		em.close();
		
	}
	
	private void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 10 명의 직원 정보 등록
		for(int i=1; i<=10; i++) {
			Employee employee = new Employee();
			employee.setName("직원" + i);
			employee.setMailId("anti-corona" + i);
			employee.setDeptName("개발부");
			employee.setSalary(12700.00 * i);
			employee.setStartDate(new Date());
			employee.setTitle("사원");
			employee.setCommisionPct(15.00);
			em.persist(employee);
			
		}
		
		em.getTransaction().commit();
		em.close();
	}
}
