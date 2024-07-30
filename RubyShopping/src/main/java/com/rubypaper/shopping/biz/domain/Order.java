package com.rubypaper.shopping.biz.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessorType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "S_ORDER")
@Data
@NoArgsConstructor
@ToString(exclude = {"customer", "itemList", "searchCustomerName", "searchOrderStatus"})
public class Order {	

	// 주문 아이디
	@Id	@GeneratedValue
	@Column(name = "ORDER_ID")
	private Long id;					
	
	// 회원 참조
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")	// FK 설정
	private Customer customer;			
	
	// 주문 상태
	// EnumType.STRING : enum 의 값 자체가 문자 그대로(ORDER, CANCEL) 테이블에 저장
	@Enumerated(EnumType.STRING)
	private OrderStatus status;			
	
	// 주문 날짜
	private Date orderDate;				

	/** 검색 관련 정보
	 *
	 * 검색 관련 정보이므로 Entity 의 멤버로 사용할 필요 X
	 * -> @Transient : 영속 대상에서 제외 -> 테이블 칼럼에서 제외
	 */
	// 검색할 회원 이름
	@Transient
	private String searchCustomerName;		
	
	// 검색할 주문 상태
	@Transient
	private OrderStatus searchOrderStatus;
	
	// 주문내역 목록
	// 관계 Entity 의 영속성 설정 -> CascadeType.ALL == Item 엔티티는 Order 엔티티의 생명주기와 함께하도록 설정
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Item> itemList = new ArrayList<Item>(); 

	// 주문 생성자 : 회원과 주문 내역 객체를 이용하여 주문을 구성한다. 
	public Order(Customer customer, Item item) {
		// 생성자를 통해 주문 엔티티 생성될 때, 바로 고객과 주문 정보의 연관관계를 맺어주기 위함
		setCustomer(customer);
		addItem(item);	
		this.status = OrderStatus.ORDER; 	// 주문 생성 시 상태는 ORDER
		this.orderDate = new Date();
	}
	
	// 회원 설정시에 회원 쪽에도 양방향 참조 설정
	public void setCustomer(Customer customer) {
		this.customer = customer;
		// 생성자를 통해 주문 엔티티가 생성되면, 고객과의 연관관계를 설정
		customer.getOrderList().add(this);
	}
	
	// 주문내역 설정 시에 주문내역에도 양방향 참조 설정
	public void addItem(Item item) {
		itemList.add(item);
		item.setOrder(this);	// 연관관계 테이블의 FK 를 설정 -> order(FK) -> 비식별 관계 유지
	}

	// 주문 취소 처리
	public void orderCancel() {
		// 주문 상태 멤버 변수를 ORDER 에서 CANCEL 로 수정
		this.setStatus(OrderStatus.CANCEL);
		for (Item item : itemList) {
			item.restoreStock();
		}
	}
}
