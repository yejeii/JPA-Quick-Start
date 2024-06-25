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

public class JPQLParamBindingTest {

	@Test
	public void ParamBindingJPQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			dataSelectByParamName(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
	/* 
	 * 파라미터 바인딩 : 인덱스 이용
	 */
	private void dataSelectByParamIndex(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT id, name, title, deptName, salary "
					+ "FROM Employee "
					+ "WHERE id = ?1 AND name = ?2";
		
		// JPQL 전송
		Query query = em.createQuery(jpql);
		query.setParameter(1, 1L);
		query.setParameter(2, "직원 1");
		
		// 검색 결과 처리
		Object[] result = (Object[]) query.getSingleResult();
		System.out.println("검색된 직원 목록");
		System.out.println(result[0] + "번 직원의 정보");
		System.out.println(Arrays.toString(result));
		
		em.close();
		
	}
	
	/*
	 * 파라미터 바인딩 : 이름 이용
	 */
	private void dataSelectByParamName(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT id, name, title, deptName, salary "
					+ "FROM Employee "
					+ "WHERE id = :employeeId AND name = :employeeName";
		
		// JPQL 전송
		Query query = em.createQuery(jpql);
		query.setParameter("employeeId", 1L);
		query.setParameter("employeeName", "직원 1");
		
		// 검색 결과 처리
		Object[] result = (Object[]) query.getSingleResult();
		System.out.println("검색된 직원 목록");
		System.out.println(result[0] + "번 직원의 정보");
		System.out.println(Arrays.toString(result));
		
		em.close();
		
	}
	
	private void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 10 명의 직원 정보 등록
		for(int i=1; i<=10; i++) {
			Employee employee = new Employee();
			employee.setName("직원 " + i);
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
