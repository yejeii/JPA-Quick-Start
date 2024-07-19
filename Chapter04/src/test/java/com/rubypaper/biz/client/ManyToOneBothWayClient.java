package com.rubypaper.biz.client;

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
			dataInsertBothMapping(emf);
			dataSelect(emf);
//			dataUpdate(emf);
			// dataDelete(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
	/** 엔티티를 영속성 컨텍스트에 등록 */
	private static void dataInsertBothMapping(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 부서 등록
		// 2. 참조 변수에 부서 정보를 설정하기 위해 부서 객체 등록 후 직원 객체에 할당
		// 	  문제 발생 : 부서에는 직원 정보 할당이 안됨 -> 양방향이 단절된 상태...
		Department department = new Department();
		department.setName("개발부");
		em.persist(department);

		// 직원 등록
		Employee employee1 = new Employee();
		employee1.setName("둘리");
		employee1.setDept(department);	// 사원 엔티티에 부서를 초기화하는 것과 동시에 부서 엔티티에 사원이 추가됨
										// 자동으로 양방향이 유지됨!
		em.persist(employee1);
		
		// 직원 등록
		Employee employee2 = new Employee();
		employee2.setName("도우너");
		employee2.setDept(department);
		em.persist(employee2);
		
		// 부서 등록
		// 1. FK 소유자가 아닌 곳에만 값을 설정하는 경우
		// 	  Department.employeeList 에 Employee 등록
		// 	  -> null 로 설정
		// Department department = new Department();
		// department.setName("개발부");
		// department.getEmployeeList().add(employee1);	
		// department.getEmployeeList().add(employee2);
		// em.persist(department);	
		
		// 3. 엔티티 양방향 설정에 부합하기 위해 부서 객체의 직원 참조 변수에 직원 등록
		// 	  문제 : 부서 엔티티에서 사원 정보 등록 기능을 부서 엔티티에서 구현하고 싶음
		// 			 즉, 자동으로 엔티티에 등록시켜 양방향 참조 관계를 유지하고 싶음
		// department.getEmployeeList().add(employee1);
		// department.getEmployeeList().add(employee2);
		
		System.out.println(department.getName() + "의 직원 수 : " + department.getEmployeeList().size());

		em.getTransaction().commit();

		// 양방향 참조되는지 확인
		// Department findDept = em.find(Department.class, 1L);
		
		// System.out.println("검색된 부서 : " + findDept.getName());
		// System.out.println("부서에 소속된 직원 명단");
		// for(Employee employee : findDept.getEmployeeList()) {
			// System.out.println(employee.getName() + "(" + employee.getDept().getName() + ")");
		// }
		// 분명 소속된 사원이 있지만 출력이 안되고 있음

		em.close();
		
	}

	/** 영속성 전이를 통한 양방향 참조 설정 */
	private static void shouldPersistenceCascade(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		// 부서 등록
		Department department = new Department();
		department.setName("해외영업부");
		// em.persist(department);

		// org.hibernate.TransientPropertyValueException: object references an unsaved transient instance 에러
		// 두 엔티티의 상태가 다른데, 양방향 관계설정으로 사원 엔티티에서 부서 정보를 설정하려고 했기 때문에 생긴 에러

		// 따라서, 정상적으로 사원이 등록되려면, 부서 정보가 영속 상태임을 확인한 후 사원 등록이 이루어져야 함.
		// 그래서.. 매번 확인할 것이냐?

		// -> 영속성 전이 활용
		// Employee에 setDept() 추가
		// department.getEmployeeList().add(사원); 설정	

		// 직원 등록 (Employee ---> Department 참조)
		Employee employee1 = new Employee();
		employee1.setName("해이해이");
		employee1.setDept(department);
		em.persist(employee1);

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

		// LEFT OUTER JOIN 일 때, 한방에 가져와서 1차 캐시에 각각 저장됨
		// -> SELECT 문 생성 X
		Employee em1 = em.find(Employee.class, 1L);
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
