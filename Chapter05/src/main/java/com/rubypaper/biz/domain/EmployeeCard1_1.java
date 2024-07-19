package com.rubypaper.biz.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "employee")	// 순환참조 문제 해결
@Entity
@Table(name = "S_EMP_CARD")
public class EmployeeCard1_1 {
	
	@Id
	@Column(name="CARD_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cardId;		// 사원증 ID
	
	@Column(name = "EXPIRE_DATE")
	private Date expireDate;	// 사원증 만료 기간
	
	private String role;		// 권한
	
	// @MapsId
	@OneToOne(optional=false, fetch=FetchType.LAZY) // inner join, Employee 엔티티에 대한 지연 페치
	@JoinColumn(name = "EMP_CARD_ID")
	private Employee1_1 employee;
	
	// 반대쪽(Employee) 객체에도 참조를 설정하는 메서드
	public void setEmployee(Employee1_1 employee) {
		this.employee = employee;
		employee.setCard(this);
	}
}
