package com.rubypaper.shopping.biz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rubypaper.shopping.biz.domain.Customer;
import com.rubypaper.shopping.biz.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	// ȸ�� ��� 
	public void insertCustomer(Customer customer) {
		customerRepository.insertCustomer(customer);
	}

	// ȸ�� �� ��ȸ
	public Customer getCustomer(Long customerId) {
		return customerRepository.getCustomer(customerId);
	}
	
	// ȸ�� ��� �˻�
	public List<Customer> getCustomerList() {
		return customerRepository.getCustomerList();
	}
}
