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
		Customer customer = customerRepository.getCustomer(customerId);
		Product product = productRepository.getProduct(productId);

		Item item = new Item (product, count);
		Order order = new Order(customer, item);

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
