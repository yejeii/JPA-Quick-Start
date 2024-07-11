package com.rubypaper.biz.client;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class JPQLFunctionClient {

    @Test
	public void funcJPQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			// dataSelectBySize(emf);
            // dataSelectByIndex(emf);
			dataSelectByDateFunc(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
    /**
	 * SIZE(컬렉션) : 컬렉션에 들어있는 객체 개수 구함
	 * 
	 * SIZE() 는 컬렉션 변수에 대해 size 변수를 사용한 것과 동일한 결과 리턴
	 * SIZE(d.employeeList) == d.employeeList.size	
	 * 
	 * 즉, JPQL 에서는 테이블명과 칼럼명을 클래스명과 변수명으로 동일하게 사용하는데,
	 * 이는 @Entity 로 인해 자바 객체로 보기 때문!!!!
	 * 엔티티를 영속성 컨텍스트에서 관리하는 자바 객체로 봐야하는 이유가 이런 이유 떄문. 
	 * 
	 * Hibernate 가 생성한 쿼리 >>
	 * 
	 * where
     *   (
     *	    select
     *           count(employeeli1_.DEPT_ID) 
     *       from
     *           S_EMP employeeli1_ 
     *       where
     *           department0_.DEPT_ID=employeeli1_.DEPT_ID
     *   ) >= 3
	 */
	private void dataSelectBySize(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		// String jpql = "SELECT d FROM Department d WHERE SIZE(d.employeeList) >= 3";
		String jpql = "SELECT d FROM Department d WHERE d.employeeList.size >= 3";
		TypedQuery<Department> query = em.createQuery(jpql, Department.class);
		
		// 검색 결과 처리
		List<Department> resultList = query.getResultList();
		for(Department department : resultList) {
			System.out.println("---> " + department);
		}
		
		em.close();
		
	}
	
	/**
	 * INDEX(컬렉션 별칭) : 컬렉션에서 특정 인덱스에 저장된 엔티티 정보를 구함
	 * (@OrderColumn)이 적용된 컬렉션에서만 사용가능
	 * 
	 * employeeList 에 @OrderColumn(name="EMP_IDX") 설정
	 * -> 영속 컨테이너, employeeList 에 저장된 직원 객체의 인덱스 정보를 테이블에도 저장함
	 * 
	 * 쿼리 결과 요약 >>
	 * 	1. S_EMP 테이블이 create 될 때, Department 엔티티를 보고 EMP_IDX 칼럼이 추가되어 create 됨
	 * 	2. S_EMP 테이블에 EMP_IDX 칼럼 빼고 insert 된 후, EMP_IDX update 가 한번 더 이루어짐
	 * 	   즉, 총 2차에 걸쳐 데이터 insert 가 됨.
	 * 	   -> Employee 엔티티를 대상으로 insert 가 이루어지므로 처음 insert 문에는 EMP_IDX 세팅이 이루어질 수 없는 것
	 *  3. 조회 쿼리 처리
	 *  Hibernate: 
	 * 	 select 
	 * 		department0_.name as col_0_0_,
	 * 		employeeli1_.EMP_IDX as col_1_0_,
	 * 		employeeli1_.id as col_2_0_,
	 * 		employeeli1_.id as id1_1_,
	 * 		employeeli1_.COMMISSION_PCT as COMMISSI2_1_,
	 * 		employeeli1_.DEPT_ID as DEPT_ID9_1_,
	 * 		employeeli1_.DEPT_NAME as DEPT_NAM3_1_,
	 * 		employeeli1_.MAIL_ID as MAIL_ID4_1_,
	 * 		employeeli1_.name as name5_1_,
	 * 		employeeli1_.salary as salary6_1_,
	 * 		employeeli1_.START_DATE as START_DA7_1_,
	 * 		employeeli1_.title as title8_1_ ,
	 * 	from 
	 * 		S_DEPT department0_ 
	 * 	inner join
	 * 		S_EMP employeeli1_ 
	 * 	on department0_.DEPT_ID=employeeli1_.DEPT_ID 
	 * 	where employeeli1_.EMP_IDX=2
	 * 
	 * Hibernate:
	 * 	select
	 * 		department0_.DEPT_ID as DEPT_ID1_0_0_,
	 * 		department0_.name as name2_0_0_ 
	 * 	from
	 * 		S_DEPT department0_ 
	 * 	where 
	 * 		department0_.DEPT_ID=?
	 * 
	 * Hibernate:
	 * 	select
	 * 		department0_.DEPT_ID as DEPT_ID1_0_0_,
	 * 		department0_.name as name2_0_0_ 
	 * 	from 
	 * 		S_DEPT department0_ 
	 * 	where 
	 * 		department0_.DEPT_ID=?
	 * 
	 */
	private void dataSelectByIndex(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT d.name, INDEX(e), e "
					+ "FROM Department d "
					+ "JOIN d.employeeList e "
					+ "WHERE INDEX(e) = 2";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		// 검색 결과 처리
		List<Object[]> resultList = query.getResultList();
		for(Object[] result : resultList) {
			Employee employee = (Employee) result[2];
			System.out.println(result[0] + " " + result[1] + "번 인덱스에 저장된 직원 : " + employee.getName());
		}
		
		em.close();
		
	}

	/**
	 * 날짜형 함수
	 * 
	 */
	private void dataSelectByDateFunc(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP "
					+ "FROM Department d ";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		
		// 검색 결과 처리
		List<Object[]> resultList = query.getResultList();
		for(Object[] result : resultList) {
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
