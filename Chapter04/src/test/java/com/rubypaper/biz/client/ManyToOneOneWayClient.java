package com.rubypaper.biz.client;

import java.rmi.activation.ActivationGroup_Stub;
import java.security.KeyStore.PrivateKeyEntry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class ManyToOneOneWayClient {

	public static void main(String[] args) {

		// 엔티티 매니저 팩토리 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter04");
		
		try {
			dataInsert(emf);
//			dataSelect(emf);
//			dataUpdate(emf);
			dataDelete(emf);
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
		em.persist(department);		// 영속 컨테이너에 관리 상태로 등록
		
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
		
		em.getTransaction().commit();
		em.close();
		
	}

	private static void dataSelect(EntityManagerFactory emf) {
		
		EntityManager em = emf.createEntityManager();
		Employee employee = em.find(Employee.class, 2L);
//		System.out.println(employee.getName() + " 직원이 검색됨");
		System.out.println(employee.getName() + "의 부서 : " + employee.getDept().getName());
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
	}
	
	private static void dataDelete(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 부서에 대한 참조 제거
		Employee employee1 = em.find(Employee.class, 1L);
		employee1.setDept(null);
		Employee employee2 = em.find(Employee.class, 2L);
		employee2.setDept(null);
		
		Department department = em.find(Department.class, 1L);
		em.remove(department);
		em.getTransaction().commit();
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
