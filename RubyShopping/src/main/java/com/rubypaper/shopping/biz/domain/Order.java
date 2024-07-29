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

	// �ֹ� ���̵�
	@Id	@GeneratedValue
	@Column(name = "ORDER_ID")
	private Long id;					
	
	// ȸ�� ����
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	private Customer customer;			
	
	// �ֹ� ����
	@Enumerated(EnumType.STRING)
	private OrderStatus status;			
	
	// �ֹ� ��¥
	private Date orderDate;				

	/** �˻� ���� ���� */	
	// �˻��� ȸ�� �̸�
	@Transient
	private String searchCustomerName;		
	
	// �˻��� �ֹ� ����
	@Transient
	private OrderStatus searchOrderStatus;
	
	// �ֹ����� ���
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Item> itemList = new ArrayList<Item>(); 

	// �ֹ� ������ : ȸ���� �ֹ� ���� ��ü�� �̿��Ͽ� �ֹ��� �����Ѵ�. 
	public Order(Customer customer, Item item) { 		
		setCustomer(customer);
		addItem(item);	
		this.status = OrderStatus.ORDER; 	// �ֹ� ���� �� ���´� ORDER
		this.orderDate = new Date();
	}
	
	// ȸ�� �����ÿ� ȸ�� �ʿ��� ����� ���� ����
	public void setCustomer(Customer customer) {
		this.customer = customer;
		customer.getOrderList().add(this);
	}
	
	// �ֹ����� ���� �ÿ� �ֹ��������� ����� ���� ����
	public void addItem(Item item) {
		itemList.add(item);
		item.setOrder(this);
	}

	// �ֹ� ��� ó��
	public void orderCancel() {
		this.setStatus(OrderStatus.CANCEL);
		for (Item item : itemList) {
			item.restoreStock();
		}
	}
}
