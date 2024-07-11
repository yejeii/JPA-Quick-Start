package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class JPQLUpdateDeleteClient {

    @Test
	public void joinJPQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			// dataUpdate(emf);
            dataUpdateAndSelect(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
	/**
	 * 수정과 캐시
	 * JPQL 을 이용해 데이터를 수정/삭제하면 해당 작업은 DB 에 바로 반영됨.
	 * -> 영속 컨테이너 캐시에 저장된 엔티티와 무관하게 처리됨
	 * -> 영속 컨테이너 캐시에 저장된 엔티티에엔 영향 X
	 * -> WHY? 영속 컨테이너에 등록된 객체를 수정한 것이 아니라, DB에 직접 UPDATE/DELETE 쿼리를 날렸기 때문
	 * 
	 * -> JPQL 을 이용해 데이터 수정/삭제시, 
	 * 	  수정이나 삭제할 데이터를 먼저 검색하지 말고 수정/삭제를 먼저 처리한 후 검색하는 걸 원칙으로 하자.
	 */
	private void dataUpdate(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		// 영속 컨테이너에 저장된 엔티티
		Employee findEmp = em.find(Employee.class, 1L);
		System.out.println("수정 전 급여: " + findEmp.getSalary());

		// JPQL
		Query query = em.createQuery("UPDATE Employee e SET e.salary = salary * 1.3 WHERE e.id =:empId");
		query.setParameter("empId", 1L);
		int updateCount = query.executeUpdate();
		System.out.println(updateCount + "건의 데이터 갱신됨");

		// 영속 컨테이너의 엔티티 수정 -> 엔티티 및 DB 반영
		// findEmp.setSalary(findEmp.getSalary() * 1.3);
		// em.getTransaction().commit();		
		
		// 수정 후 영속 컨테이너에 저장된 엔티티 확인
		System.out.println("수정 후 급여 : " + findEmp.getSalary());
		
		em.getTransaction().commit();		
		em.close();
	}

	/**
	 * 위 메서드를 수정
	 * 
	 * 원칙 : 수정/삭제 -> 검색
	 */
	private void dataUpdateAndSelect(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		// JPQL
		Query query = em.createQuery("UPDATE Employee e SET e.salary = salary * 1.3 WHERE e.id =:empId");
		query.setParameter("empId", 1L);
		int updateCount = query.executeUpdate();
		System.out.println(updateCount + "건의 데이터 갱신됨");

		// 수정된 데이터를 영속 컨테이너가 관리할 엔티티로 저장
		String jpql = "SELECT e FROM Employee e WHERE e.id = 1L";
		query = em.createQuery(jpql);
		Employee employee = (Employee) query.getSingleResult();
		System.out.println(employee.getId() + "번 직원의 수정된 급여 : " + employee.getSalary());
		
		em.getTransaction().commit();		
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
