package com.rubypaper.shopping.biz.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "S_PRODUCT")
@Data
public class Product {

	// 상품 아이디
	@Id	@GeneratedValue
	@Column(name = "PRODUCT_ID")
	private Long id;

	// 상품 이름
	private String name;

	// 상품 가격
	private int price;

	// 상품 수량
	private int quantity;

	// 주문 정보 생성 시에 재고 수량을 감소시킨다.
	public void reduceStock(int q) {
		this.quantity = this.quantity - q;
		// 재고 수량이 부족하면 예외를 발생시킨다.
		if (quantity < 0) {
			throw new IllegalStateException("재고가 부족합니다.");
		}
	}

	// 주문 취소 시에 재고 수량을 원래대로 되돌린다.
	public void restoreStock(int quantity) {
		this.quantity = this.quantity + quantity;
	}
}
