package com.rubypaper.shopping.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rubypaper.shopping.biz.domain.Order;
import com.rubypaper.shopping.biz.domain.OrderStatus;
import com.rubypaper.shopping.biz.service.CustomerService;
import com.rubypaper.shopping.biz.service.OrderService;
import com.rubypaper.shopping.biz.service.ProductService;

@Controller
public class OrderController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;

	// 주문 등록 화면으로 이동
	@GetMapping("/order/new")
	public String insertOrder(Model model) {
		// 주문 등록 화면에서 사용할 회원 목록과 상품 목록 조회
		model.addAttribute("customerList", customerService.getCustomerList());
		model.addAttribute("productList", productService.getProductList());
		return "order/insertOrder";
	}
	
	// 주문 등록 기능 처리
	@PostMapping("/order/new")
	public String insertOrder(Long customerId, Long productId, int count) {
		orderService.insertOrder(customerId, productId, count);
		return "redirect:/getOrderList";
	}

	// 주문 목록 검색 기능 처리
	@RequestMapping("/getOrderList")
	public String getOrderList(@ModelAttribute("searchInfo") Order order, 
		Model model) {
		List<Order> orderList = orderService.getOrderList(order);
		model.addAttribute("orderList", orderList);
		return "order/getOrderList";
	}

	// 주문 취소 처리
	@GetMapping("/order/{orderId}/orderCancel")
	public String orderCancel(@PathVariable("orderId") Long orderId) {
		orderService.orderCancel(orderId);
		return "redirect:/getOrderList";
	}

}
