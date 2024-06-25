package com.rubypaper.biz.client;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.rubypaper.biz.domain.Order;
import com.rubypaper.biz.domain.Product;

/**
 * 연결 엔티티 없이 다대다 양방향 매핑 테스트
 * 
 * @author ga29_
 *
 */
public class ManyToManyBothWayNoRelationEntityClient {

	public static void main(String[] args) {
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
		
		// 검색한 Order 를 통해 Product 목록을 출력한다.
		Order order = em.find(Order.class, 1L);
		System.out.println(order.getId() + "번 주문 시간 : " + order.getOrderDate());
		System.out.println("상품 목록 -------------");
		
//		List<Product> productList = order.getProductList();
//		for(Product product : productList) {
//			System.out.println("---> " + product.getName());
//		}
		
		// 검색한 Product 를 통해 Order 목록 출력
		Product product = em.find(Product.class, 1L);
		
		System.out.println(product.getName() + " 상품에 대한 주문 정보");
//		List<Order> orderList = product.getOrderList();
//		for(Order ord : orderList) {
//			System.out.println("---> " + ord.toString());
//		}
		
	}

	private static void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 1번 상품 등록
		Product product1 = new Product();
		product1.setName("삼성 TV");
		em.persist(product1);
		
		// 2번 상품 등록
		Product product2 = new Product();
		product2.setName("삼성 S24");
		em.persist(product2);
		
		// 1번 주문 등록
		Order order1 = new Order();
		order1.setOrderDate(new Date());
		// 주문 객체가 가진 상품 목록(productList)에 상품 저장
//		order.getProductList().add(product1);
//		order.getProductList().add(product2);
//		order1.addProduct(product1);
		em.persist(order1);
		
		// 2번 주문 등록
		Order order2 = new Order();
		order2.setOrderDate(new Date());
//		order2.addProduct(product1);
		em.persist(order2);
		
		em.getTransaction().commit();
		em.close();
		
	}

}
