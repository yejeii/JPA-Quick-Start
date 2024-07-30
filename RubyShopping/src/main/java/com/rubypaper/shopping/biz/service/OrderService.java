package com.rubypaper.shopping.biz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rubypaper.shopping.biz.domain.Customer;
import com.rubypaper.shopping.biz.domain.Order;
import com.rubypaper.shopping.biz.domain.Item;
import com.rubypaper.shopping.biz.domain.Product;
import com.rubypaper.shopping.biz.repository.CustomerRepository;
import com.rubypaper.shopping.biz.repository.OrderRepository;
import com.rubypaper.shopping.biz.repository.ProductRepository;

@Service
@Transactional
public class OrderService {
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	// 주문 등록
	public void insertOrder(Long customerId, Long productId, int count) {

		// 1. 주문을 위해서 회원과 상품에 대한 Entity 가 필요
		// 	  영속성 컨테이너에 회원 정보에 대한 Entity, 상품에 대한 Entity 가 존재해야만 주문처리가 가능
		Customer customer = customerRepository.getCustomer(customerId);
		Product product = productRepository.getProduct(productId);

		// 2. 비식별자 관계를 고려해서 Item Entity, Order Entity 가 필요
		// 	  Order Entity 의 PK 가 Item Entity 의 FK 가 되어야 함
		Item item = new Item (product, count);
		Order order = new Order(customer, item);

		// 3. Order Entity 내부에서 연관관계 설정
		// 	  - Item 생성자 : product.reduceStock(), 재고 수량에서 주문 수량을 차감
		// 	  - Order 생성자 : 생성자를 통해 주문 엔티티가 생성될 때,
		// 					  바로 고객과 주문 정보의 연관 관계를 맺어주기 위함
		//
		// 					  비식별자 관계 유지

		orderRepository.insertOrder(order);
	}

	// 주문 취소
	public void orderCancel(Long orderId) {
		Order order = orderRepository.getOrder(orderId);
		order.orderCancel();
	}

	// 주문 목록 검색
	public List<Order> getOrderList(Order order) {
		return orderRepository.getOrderList(order);
	}
}
