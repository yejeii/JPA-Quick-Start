package com.rubypaper.shopping.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.rubypaper.shopping.biz.domain.Address;
import com.rubypaper.shopping.biz.domain.Customer;
import com.rubypaper.shopping.biz.service.CustomerService;

@Controller
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;

	// 회원 등록 화면으로 이동
	@GetMapping("/customer/new")
	public String insertCustomer() {
		return "customer/insertCustomer";
	}

	// 회원 등록 기능 처리
	@PostMapping("/customer/new")
	public String insertCustomer(Customer customer, Address address) {
		customer.setAddress(address);
		customerService.insertCustomer(customer);
		return "redirect:/getCustomerList";
	}

	// 회원 목록 조회 기능 처리
	@GetMapping("/getCustomerList")
	public String getCustomerList(Model model) {
		model.addAttribute("customerList", customerService.getCustomerList());
		return "customer/getCustomerList";
	}

}
