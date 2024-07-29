package com.rubypaper.shopping;

import java.util.List;

import org.springframework.context.support.GenericXmlApplicationContext;

import com.rubypaper.shopping.biz.domain.Address;
import com.rubypaper.shopping.biz.domain.Customer;
import com.rubypaper.shopping.biz.domain.Order;
import com.rubypaper.shopping.biz.domain.Product;
import com.rubypaper.shopping.biz.service.CustomerService;
import com.rubypaper.shopping.biz.service.OrderService;
import com.rubypaper.shopping.biz.service.ProductService;

public class RubbyShoppingClient {

	public static void main(String[] args) {
		// 스프링 컨테이너 생성
		String configLocation = "classpath:spring/business-layer.xml";
		GenericXmlApplicationContext container = 
			new GenericXmlApplicationContext(configLocation);
		
		// 서비스 컴포넌트 검색(Lookup)
		CustomerService customerService = 
	 	 	 (CustomerService) container.getBean("customerService");
		ProductService productService = 
	 	 	 (ProductService) container.getBean("productService");
		OrderService orderService = 
	 	 	 (OrderService) container.getBean("orderService");

		insertData(customerService, productService, orderService);
		selectData(customerService);
		
		// 스프링 컨테이너 종료
		container.close();
	}

	private static void selectData(CustomerService customerService) {
		Customer customer = customerService.getCustomer(1L);
		System.out.println(customer.toString());
		List<Order> orderList = customer.getOrderList();
		for (Order order : orderList) {
			System.out.println(order.toString());
		}
	}

	private static void insertData(CustomerService customerService, 
					ProductService productService, 
					OrderService orderService) {
		// 회원 정보 생성 및 등록
		Customer customer = new Customer();
		customer.setName("홍길동");
		Address address = new Address();
		address.setCity("서울시");
		address.setRoadName("행당로 82");
		address.setZipCode("123-123");		
		customer.setAddress(address);
		customer.setPhone("011-1234-5678");
		customer.setComments("반품 요청이 많은 회원임");

		customerService.insertCustomer(customer);

		// 두 개의 상품 정보 생성 및 등록
		Product product1 = new Product();
		product1.setName("JPA 처음 가보는 길");
		product1.setPrice(20000);
		product1.setQuantity(10);
		productService.insertOrUpdateProduct(product1);
		
		Product product2 = new Product();
		product2.setName("자바 프로그래밍 기초");
		product2.setPrice(40000);
		product2.setQuantity(20);
		productService.insertOrUpdateProduct(product2);
		
		// 주문 정보 생성
		orderService.insertOrder(customer.getId(), product1.getId(), 5);
	}
}
