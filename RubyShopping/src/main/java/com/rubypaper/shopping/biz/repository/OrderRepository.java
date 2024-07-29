package com.rubypaper.shopping.biz.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.rubypaper.shopping.biz.domain.Customer;
import com.rubypaper.shopping.biz.domain.Order;

@Repository
public class OrderRepository {
	
	@PersistenceContext
	private EntityManager em;

	// �ֹ� ��� 
	public void insertOrder(Order order) {
		em.persist(order);
	}

	// �ֹ� �� ��ȸ 
	public Order getOrder(Long id) {
		return em.find(Order.class, id);
	}

	// �ֹ� ��� �˻� 
	public List<Order> getOrderList(Order order) {
		// Criteria�� �̿��� ���� ����
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);

		// FROM Order ord
		Root<Order> ord = criteriaQuery.from(Order.class);

		List<Predicate> criteria = new ArrayList<Predicate>();

		if (order.getSearchCustomerName() != null) {
			// and m.name like %order.customerName%
			Predicate name = builder.like(ord.<Customer>get("customer").<String>get("name"), "%" + order.getSearchCustomerName() + "%");
			criteria.add(name);
		}
		
		if (order.getSearchOrderStatus() != null) {
//			// INNER JOIN emp.dept dept
//			Join<Order, Department> dept = ord.join("dept");

			
			// ord.status == order.searchOrderStatus
			Predicate status = builder.equal(ord.get("status"), order.getSearchOrderStatus());
			criteria.add(status);
		}

		// Predicate �迭�� �̿��Ͽ� ���� WHERE�� ����
		criteriaQuery.where(builder.and(criteria.toArray(new Predicate[criteria.size()])));
		
		// SELECT ����
		TypedQuery<Order> query = em.createQuery(criteriaQuery).setMaxResults(1000);
		return query.getResultList();
	}
}
