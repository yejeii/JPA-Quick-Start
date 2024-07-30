package com.rubypaper.shopping.biz.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.rubypaper.shopping.biz.domain.Customer;

@Repository
public class CustomerRepository {

	@PersistenceContext
	private EntityManager em;

	// 회원 등록
	public void insertCustomer(Customer customer) {
		em.persist(customer);
	}

	// 회원 상세 조회
	public Customer getCustomer(Long id) {
		return em.find(Customer.class, id);
	}

	// 회원 목록 검색
	public List<Customer> getCustomerList() {
		return em.createQuery("SELECT c FROM Customer c ORDER BY c.id", 
		Customer.class).getResultList();
	}
}
