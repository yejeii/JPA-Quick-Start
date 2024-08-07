package com.rubypaper.biz.client;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.domain.Card;

/** 일대일 단방향 매핑 테스트 */
public class OneToOneOneWayClient {
	
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
		
		Card card = em.find(Card.class, 1L);
		System.out.println("--");
		System.out.println("검색된 사원증 번호 : " + card.getCardId());
		System.out.println("--");
		System.out.println("권한 : " + card.getRole());
		System.out.println("--");
		System.out.println("사원증 소유자 : " + card.getEmployee().getName());
		
	}

	private static void dataInsert(EntityManagerFactory emf) throws ParseException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 직원 등록
		Employee employee = new Employee();
		employee.setName("둘리");
		em.persist(employee);
		
		// 사원증 등록
		Card card = new Card();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		card.setExpireDate(dateFormat.parse("2025-12-31"));
		card.setRole("MASTER");

		// 직원에 대한 참조 설정
		card.setEmployee(employee);
		em.persist(card);
		
		em.getTransaction().commit();
		em.close();
	}

}
