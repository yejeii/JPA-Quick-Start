package com.rubypaper.biz.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

/** 양방향 통신 테스트 */
public class ManyToOneBothWayClient {

	@Test
	public void run() {

		// 엔티티 매니저 팩토리 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter04");
		
		try {
			dataInsert(emf);
			dataSelect(emf);
//			dataUpdate(emf);
			// dataDelete(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
	private static void dataInsert(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 부서 등록
		Department department = new Department();
		department.setName("개발부");
		em.persist(department);	
		
		// 직원 등록
		Employee employee1 = new Employee();
		employee1.setName("둘리");
		employee1.setDept(department);
		em.persist(employee1);
		
		// 직원 등록
		Employee employee2 = new Employee();
		employee2.setName("도우너");
		employee2.setDept(department);
		em.persist(employee2);


		// 직원 등록 (Employee ---> Department 참조)
//		Employee employee1 = new Employee();
//		employee1.setName("둘리");
//		employee1.setDept(department);
//		em.persist(employee1);
//		
//		// 직원 등록 (Employee ---> Department 참조)
//		Employee employee2 = new Employee();
//		employee2.setName("도우너");
//		employee2.setDept(department);
//		em.persist(employee2);
		
		// 직원 여러명 등록
		// for (int i = 1; i <= 5; i++) {
			// Employee employee = new Employee();
			// employee.setName("직원-" + i);
			// employee.setDept(department);
		// }
		// em.persist(department);
		
		// Department.employeeList 에 Employee 등록
//		department.getEmployeeList().add(employee1);
//		department.getEmployeeList().add(employee2);
		
		// System.out.println(department.getName() + "의 직원 수 : " + department.getEmployeeList().size());
		
		em.getTransaction().commit();
		em.close();
		
	}

	private static void dataSelect(EntityManagerFactory emf) {
		
		EntityManager em = emf.createEntityManager();
		Department department = em.find(Department.class, 1L);
		
		System.out.println("검색된 부서 : " + department.getName());
		System.out.println("부서에 소속된 직원 명단");
		for(Employee employee : department.getEmployeeList()) {
			System.out.println(employee.getName() + "(" + employee.getDept().getName() + ")");
		}
		em.close();
	}
	
	private static void dataUpdate(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 신규 부서 등록
		Department department = new Department();
		department.setName("영업부");
		em.persist(department);
		
		// 부서 변경
		Employee employee = em.find(Employee.class, 1L);
		employee.setDept(department);
		
		em.getTransaction().commit();
		em.close();
	}
	
	private static void dataDelete(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 부서 검색
		Department department = em.find(Department.class, 1L);
		
		// 부서에 등록된 직원 수정
		List<Employee> employeeList = department.getEmployeeList();
		for(Employee employee : employeeList) {
			//em.remove(employee);
			employee.standby();
		}
		
		// 부서에 속한 모든 직원을 컬렉션에서 제거
//		List<Employee> employeeList = department.getEmployeeList();
//		employeeList.clear();	// 영속 컨테이너에서 분리
		
		// 부서 삭제
		em.remove(department);
		
		em.getTransaction().commit();
		em.close();
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
