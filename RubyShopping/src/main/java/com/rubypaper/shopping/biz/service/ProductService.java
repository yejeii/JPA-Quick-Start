package com.rubypaper.shopping.biz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rubypaper.shopping.biz.domain.Product;
import com.rubypaper.shopping.biz.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	// ��ǰ ��� Ȥ�� ����
	public void insertOrUpdateProduct(Product product) {
		productRepository.insertOrUpdateProduct(product);
	}

	// ��ǰ �� ��ȸ
	public Product getProduct(Long productId) {
		return productRepository.getProduct(productId);
	}

	// ��ǰ ��� �˻�
	public List<Product> getProductList() {
		return productRepository.getProductList();
	}
}
