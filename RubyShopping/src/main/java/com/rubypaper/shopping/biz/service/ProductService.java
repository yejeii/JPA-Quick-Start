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

	// 상품 등록 혹은 수정
	public void insertOrUpdateProduct(Product product) {
		productRepository.insertOrUpdateProduct(product);
	}

	// 상품 상세 조회
	public Product getProduct(Long productId) {
		return productRepository.getProduct(productId);
	}

	// 상품 목록 검색
	public List<Product> getProductList() {
		return productRepository.getProductList();
	}
}
