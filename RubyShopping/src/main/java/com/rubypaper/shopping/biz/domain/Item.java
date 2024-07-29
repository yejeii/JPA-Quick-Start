package com.rubypaper.shopping.biz.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "S_ITEM")
@Data
@NoArgsConstructor
@ToString(exclude = {"order", "product"})
public class Item {

	// 주문내역 아이디
	@Id	@GeneratedValue
	@Column(name = "ITEM_ID")
	private Long id;

	// 주문 참조
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID")
	private Order order;

	// 상품 참조
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	
	// 주문 수량
	private int count;

	// 주문내역 생성자
	public Item(Product product, int count) {
		this.product = product;
		this.count = count;

		// 주문이 생성된 순간 주문 수량만큼 재고를 감소한다.
		reduceStock(count);
	}

	// 주문 발생 시에 상품 재고량을 감소시킨다.
	public void reduceStock(int count) {
		product.reduceStock(count);
	}

	// 주문 취소 시에 재고량을 원래대로 되돌린다.
	public void restoreStock() {
		product.restoreStock(count);
	}
}
