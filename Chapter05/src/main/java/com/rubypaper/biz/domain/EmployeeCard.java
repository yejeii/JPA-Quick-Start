package com.rubypaper.biz.domain;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "employee")	// 순환참조 문제 해결
@Entity
@Table(name = "S_EMP_CARD")
public class EmployeeCard {
	
	@Id
	@Column(name="CARD_ID")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cardId;		// 사원증 ID
	
	@Column(name = "EXPIRE_DATE")
	private Date expireDate;	// 사원증 만료 기간
	
	private String role;		// 권한
	
//	@OneToOne(optional = false,			// inner join 으로 고정
//				fetch = FetchType.LAZY) // Employee 엔티티에 대한 지연 페치 : 해당 정보를 직접 사용해야 할 때 select 문으로 가져온다.
//	@JoinColumn(name = "EMP_CARD_ID")
	@MapsId
	@OneToOne
	@JoinColumn(name = "EMP_ID")
	private Employee employee;
	
	// 반대쪽(Employee) 객체에도 참조를 설정하는 메서드
//	public void setEmployee(Employee employee) {
//		this.employee = employee;
//		employee.setCard(this);
//	}
}
