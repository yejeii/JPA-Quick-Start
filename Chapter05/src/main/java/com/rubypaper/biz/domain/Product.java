package com.rubypaper.biz.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "S_PRODUCT")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(name = "SHORT_DESC")
	private String shortDesc;
	
	private String category;
	
	// 양방향 매핑
	@OneToMany(mappedBy = "product")	// 연관관계의 소유자가 Item.product 변수임을 선언
	private List<Item> itemList = new ArrayList<>();
}
