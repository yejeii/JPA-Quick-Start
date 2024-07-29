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
		// ������ �����̳� ����
		String configLocation = "classpath:spring/business-layer.xml";
		GenericXmlApplicationContext container = 
			new GenericXmlApplicationContext(configLocation);
		
		// ���� ������Ʈ �˻�(Lookup)
		CustomerService customerService = 
	 	 	 (CustomerService) container.getBean("customerService");
		ProductService productService = 
	 	 	 (ProductService) container.getBean("productService");
		OrderService orderService = 
	 	 	 (OrderService) container.getBean("orderService");

		insertData(customerService, productService, orderService);
		selectData(customerService);
		
		// ������ �����̳� ����
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
		// ȸ�� ���� ���� �� ���
		Customer customer = new Customer();
		customer.setName("ȫ�浿");
		Address address = new Address();
		address.setCity("�����");
		address.setRoadName("���� 82");
		address.setZipCode("123-123");		
		customer.setAddress(address);
		customer.setPhone("011-1234-5678");
		customer.setComments("��ǰ ��û�� ���� ȸ����");

		customerService.insertCustomer(customer);

		// �� ���� ��ǰ ���� ���� �� ���
		Product product1 = new Product();
		product1.setName("JPA ó�� ������ ��");
		product1.setPrice(20000);
		product1.setQuantity(10);
		productService.insertOrUpdateProduct(product1);
		
		Product product2 = new Product();
		product2.setName("�ڹ� ���α׷��� ����");
		product2.setPrice(40000);
		product2.setQuantity(20);
		productService.insertOrUpdateProduct(product2);
		
		// �ֹ� ���� ����
		orderService.insertOrder(customer.getId(), product1.getId(), 5);
	}
}
