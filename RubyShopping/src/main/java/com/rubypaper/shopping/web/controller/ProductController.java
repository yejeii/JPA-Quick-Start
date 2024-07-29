package com.rubypaper.shopping.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.rubypaper.shopping.biz.domain.Product;
import com.rubypaper.shopping.biz.service.ProductService;

@Controller
public class ProductController {
	@Autowired
	private ProductService productService;

	// 상품 등록 화면으로 이동
	@GetMapping("/product/new")
	public String insertProduct() {
		return "product/insertProduct";
	}

	// 상품 등록 기능 처리
	@PostMapping("/product/new")
	public String insertProduct(Product product) {
		productService.insertOrUpdateProduct(product);
		return "redirect:/getProductList";
	}
	
	// 상품 정보 상세 화면으로 이동
	@GetMapping("/product/{productId}/get")
	public String getProduct(@PathVariable("productId") Long productId, Model model) {
		Product product = productService.getProduct(productId);
		model.addAttribute("product", product);
		return "product/getProduct";
	}

	
	// 상품 목록 기능 처리
	@GetMapping("/getProductList")
	public String getProductList(Model model) {
		List<Product> productList = productService.getProductList();
		model.addAttribute("productList", productList);
		return "product/getProductList";
	}

}
