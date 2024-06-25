package com.rubypaper.biz.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "itemList")
@Entity
@Table(name = "S_ORD")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "CUSTOMER_ID")
	private Long customerID;
	
	@Column(name = "ORDER_DATE")
	private Date orderDate;
	
	private Double total;
	
//	@ManyToMany
//	@JoinTable(name = "S_ITEM", 
//				joinColumns = @JoinColumn(name="ORD_ID"), 
//				inverseJoinColumns = @JoinColumn(name="PRODUCT_ID"),
//				uniqueConstraints = @UniqueConstraint(columnNames = {"ORD_ID", "PRODUCT_ID"})
//			)
//	private List<Product> productList = new ArrayList<Product>();
	
	// 상품(Product)을 등록할 때, 상품 쪽에 주문(Order) 정보도 함께 설정
//	public void addProduct(Product product) {
//		productList.add(product);
		
		// Product 에서도 주문에 대한 참조 정보 설정
//		product.getOrderList().add(this);
//	}
	
	@OneToMany(mappedBy = "order")
	private List<Item> itemList = new ArrayList<Item>();

}
