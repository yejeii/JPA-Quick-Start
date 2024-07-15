package com.rubypaper.biz.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/*
 * @Column 에 columnDefinition 속성 추가
 * 
 * @Column(precision = 11, scale = 2)
 * precision : 숫자 관련(소수점) 데이터의 전체 길이
 * scale : 소수점 데이터의 길이
 * 
 * 주의!!
 * 엔티티의 멤버가 Double, Float 타입인 경우, precision, scale 에 대한 설정은 무시됨
 * 	-> ex. precision = 2, scale = 1
 * 		   setCommisionPct(12.5550); 
 * 	-> 무시되어 정상 입력됨!!
 */
@Data
// @Entity
@Table(name="Employee4", uniqueConstraints={@UniqueConstraint(columnNames={"NAME", "MAILID"})})
public class Employee4 {
	
	@Id	// S_EMP 테이블의 PK 와 매핑
	@Column(length = 7, nullable = false)
	private Long id;
	
	@Column(length = 25, nullable = false)
	private String name;
	
	@Column(length = 8, unique = true)
	private String mailId;
	
	@Column(name="START_DATE")// 멤버명과 S_EMP 테이블의 칼럼명이 차이
								// 매핑될 칼럼명을 명시적으로 매핑
	private Date startDate;
	
	@Column(length = 25)
	private String title;
	
	@Column(name = "DEPT_NAME", length = 30)
	private String deptName;
	
	@Column(precision = 11, scale = 2)
	private Double salary;	
	
	@Column(name = "COMMISSION_PCT", precision = 2, scale = 1, 
			columnDefinition = "double CHECK(commission_pct IN(10, 12.5, 15, 17.5, 20))")
	private Double commisionPct;	// Java 의 Double 형은 precision = 2, scale = 1 이 무시됨
}