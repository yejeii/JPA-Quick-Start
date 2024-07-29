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

	// ȸ�� ���
	public void insertCustomer(Customer customer) {
		em.persist(customer);
	}

	// ȸ�� �� ��ȸ
	public Customer getCustomer(Long id) {
		return em.find(Customer.class, id);
	}

	// ȸ�� ��� �˻�
	public List<Customer> getCustomerList() {
		return em.createQuery("SELECT c FROM Customer c ORDER BY c.id", 
		Customer.class).getResultList();
	}
}
