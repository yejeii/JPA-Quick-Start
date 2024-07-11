package com.rubypaper.biz.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class JPQLOperatorClient {

    @Test
	public void joinJPQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			// dataSelectByIsEmpty(emf);
            dataSelectByMemberOf(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
    /** 컬렉션 연산자 : is empty
	 *	컬렉션이 비어있는지를 확인
     *
     * 직원이 없는 신생 부서 정보만 조회
	 * 여기선 부서 객체의 컬렉션 변수(Department.employeeList)가 비어 있는 컬렉션을 참조하는지 확인
     * 
     * Hibernate 가 생성한 쿼리 >>
     * 
     * select
     *  department0_.DEPT_ID as DEPT_ID1_0_, 
     *  department0_.name as name2_0_ 
     * from
     *  S_DEPT department0_ 
     * where
     *  not ( exists ( select
     *                  employeeli1_.id 
     *                 from
     *                  S_EMP employeeli1_ 
     *                 where
     *                  department0_.DEPT_ID=employeeli1_.DEPT_ID
     *               )
     *      )
     */
	private void dataSelectByIsEmpty(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT d FROM Department d WHERE d.employeeList is empty";
		TypedQuery<Department> query = em.createQuery(jpql, Department.class);
		
		// 검색 결과 처리
		List<Department> resultList = query.getResultList();
		System.out.println("직원이 없는 부서 목록");
		for(Department department : resultList) {
			System.out.println(department.getName());
		}
		
		em.close();
		
	}
	
	/** 컬렉션 연산자 : member (of)	
	 * 특정 엔티티가 컬렉션에 포함되어 있는지를 확인
	 * 
	 * 6번 직원이 소속되어 있는 부서 목록 출력
	 * 
	 * Hibernate 가 생성한 쿼리 >>
	 * 
	 * select
     * 	department0_.DEPT_ID as DEPT_ID1_0_,
     *  department0_.name as name2_0_ 
     * from
     * 	S_DEPT department0_ 
     * where
     *   ? in (
     *       select
     *           employeeli1_.id 
     *       from
     *           S_EMP employeeli1_ 
     *       where
     *           department0_.DEPT_ID=employeeli1_.DEPT_ID
     *   )
	 */
	private void dataSelectByMemberOf(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// JPQL
		String jpql = "SELECT d FROM Department d WHERE :employee member of d.employeeList";
		TypedQuery<Department> query = em.createQuery(jpql, Department.class);
		
        // 6번 직원 객체를 검색해서 파라미터로 설정
        Employee findEmp = em.find(Employee.class, 6L);
        query.setParameter("employee", findEmp);

		// 검색 결과 처리
		List<Department> resultList = query.getResultList();
		System.out.println(findEmp.getId() + "번 직원이 소속되어 있는 부서 목록");
		for(Department department : resultList) {
			System.out.println(department.getName());
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
