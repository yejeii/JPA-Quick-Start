package com.rubypaper.persistence.hibernate;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity	// Hibernate 에서 관리되는 엔티티 클래스임을 명시 -> H2 테이블과 매핑
@Table(name = "S_EMP")	// EmployeeVO 는 S_EMP 테이블과 매핑됨을 명시
public class EmployeeVO {
	
	@Id	// S_EMP 테이블의 PK 와 매핑
	private Long id;
	
	private String name;
	
	@Column(name = "START_DATE") // 멤버명과 S_EMP 테이블 칼럼명이 다른경우 매핑될 컬럼명을 명시	
	private Timestamp startDate;
	
	private String title;

	@Column(name = "DEPT_NAME")
	private String deptName;
	
	private Double salary;
	
	private String email;
	
}
