package com.rubypaper.biz.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/*
 * @Table 에 uniqueConstraints 속성 추가
 * NAME, MAILID 두 칼럼을 대상으로 unique 제약조건 생성.
 * 
 * 1. 테이블에 uniqueConstraints 생성유무 확인
 *    NAME, MAILID 의 두개의 칼럼으로 복합 unique 제약조건이 생성되었는지 확인
 * 2. uniqueConstraints 동작 테스트 -> Employee3ServiceTest
 * 3. 테스트 데이터 생성
 * 	  1L hong, 홍길동 : insert 성공
 *    2L hong, 홍길동 : unique 제약 조건 위배
 *    2L hong2, 홍길동 : insert 성공
 *    3L hong, 홍길동3 : insert 성공
 *    4L hong4, 홍길동4 : insert 성공
 *    
 * 그래서, 정말 중요한 것은 생성되는 데이터가 처음부터 깨끗하게 만들어지지
 * 않는다면, 향후에 개발중에서 어뚱한 데이터가 나오게 됨.
 * ( 들어가는 것이 깨끗해야 나오는 것도 깨끗함. )
 */

@Data
// @Entity
@Table(name="Employee3", uniqueConstraints={@UniqueConstraint(columnNames={"NAME", "MAILID"})})
public class Employee3 {
	
	@Id// S_EMP 테이블의 PK 와 매핑
	private Long id;
	
	private String name;
	
	private String mailId;
	
	@Column(name="START_DATE")// 멤버명과 S_EMP 테이블의 칼럼명이 차이
								// 매핑될 칼럼명을 명시적으로 매핑
	private Date startDate;
	
	private String title;
	
	@Column(name="DEPT_NAME")	
	private String deptName;
	
	private Double salary;	
	
	@Column(name = "COMMISITION_PCT")
	private Double commisionPct;
}