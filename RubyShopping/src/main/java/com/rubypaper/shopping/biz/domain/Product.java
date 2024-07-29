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

	// ��ǰ ���̵�
	@Id	@GeneratedValue
	@Column(name = "PRODUCT_ID")
	private Long id;

	// ��ǰ �̸�
	private String name;

	// ��ǰ ����
	private int price;

	// ��ǰ ����
	private int quantity;

	// �ֹ� ���� ���� �ÿ� ��� ������ ���ҽ�Ų��.
	public void reduceStock(int quantity) {
		this.quantity = this.quantity - quantity;
		// ��� ������ �����ϸ� ���ܸ� �߻���Ų��.
		if (quantity < 0) {
			throw new IllegalStateException("��� �����մϴ�.");
		}
	}

	// �ֹ� ��� �ÿ� ��� ������ ������� �ǵ�����.
	public void restoreStock(int quantity) {
		this.quantity = this.quantity + quantity;
	}
}
