package com.rubypaper.biz.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * @Temporal
 * 
 * 날짜 데이터 : 날짜 정보 + 시간 정보
 *          - 날짜 정보
 *          - 시간 정보
 *          - 날짜 정보 + 시간 정보
 */
@Data
// @Entity
@Table(name="Employee5", uniqueConstraints={@UniqueConstraint(columnNames={"NAME", "MAILID"})})
public class Employee5 {
	
	@Id	// S_EMP 테이블의 PK 와 매핑
	private Long id;
	
	private String name;
	
	private String mailId;
	
	@Column(name="START_DATE")
    @Temporal(TemporalType.DATE)
	private Date startDate;
	
	private String title;
	
	@Column(name = "DEPT_NAME")
	private String deptName;
	
	private Double salary;	
	
	private Double commisionPct;
}