package com.rubypaper.biz.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.domain.Card;

/** 일대일 양방향 매핑 테스트 */
public class OneToOneBothWayClient {
	
	@Test
	public void run() {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter05");
		try {
			dataInsert(emf);
			dataSelect(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

	private static void dataSelect(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		
		// 검색된 사원증을 통해 직원 정보 사용하기
		Card card = em.find(Card.class, 1L);
		// System.out.println("사원증을 통한 직원 정보 접근 : " + card.toString());
		System.out.println("검색된 사원증 유효기간 : " + card.getExpireDate());
		System.out.println("사원증 소유자 : " + card.getEmployee().getName());
		
		// 검색된 직원을 통해 사원증 정보 사용하기
		Employee employee = em.find(Employee.class, 1L);
		System.out.println("사원증 소유자 : " + employee.getName());
		// System.out.println("사원증 유효기간 : " + employee.getCard().getExpireDate());
		// System.out.println("직원을 통한 사원증 정보 접근 : " + employee.getCard().toString());
	}

	private static void dataInsert(EntityManagerFactory emf) throws ParseException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		// 직원 등록(부모 테이블)
		Employee employee = new Employee();
		employee.setName("둘리");
		// 사원증에 대한 참조 설정
//		employee.setCard(card);
		em.persist(employee);

		// 사원증 등록(자식 테이블)
		Card card = new Card();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		card.setExpireDate(dateFormat.parse("2025-12-31"));
		card.setRole("MASTER");
		// 직원에 대한 참조 설정
		card.setEmployee(employee);
		em.persist(card);
		
		em.getTransaction().commit();
		em.close();
		
		// 순수 객체로서의 양방향 참조
		System.out.println("사원증을 통한 직원 정보 접근 : " + card.getEmployee().getName());
		// System.out.println("직원을 통한 사원증 정보 접근 : " + employee.getCard().getExpireDate());
	}

}
