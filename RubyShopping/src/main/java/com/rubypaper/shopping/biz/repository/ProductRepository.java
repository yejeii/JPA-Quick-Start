package com.rubypaper.shopping.biz.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.rubypaper.shopping.biz.domain.Product;

@Repository
public class ProductRepository {

	@PersistenceContext
	private EntityManager em;

	// ��ǰ ��� �Ǵ� ����
	public void insertOrUpdateProduct(Product product) {
		// �Ķ���ͷ� ���޵� ��ǰ�� ���̵� ���� ���ο� ���� ���/������ �б� ó���ȴ�.
		if (product.getId() == null) {
			em.persist(product);
		} else {
			em.merge(product);
		}
	}

	// ��ǰ �� ��ȸ
	public Product getProduct(Long id) {
		return em.find(Product.class, id);
	}

	// ��ǰ ��� ��ȸ
	public List<Product> getProductList() {
		return em.createQuery("SELECT p FROM Product p ORDER BY p.id", Product.class).getResultList();
	}
}
