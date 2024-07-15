package com.rubypaper.biz.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.Data;

/**
 * @Transient
 * 
 * DB 테이블 컬럼과 매핑 시 제외하는 컬럼임을 명시
 * 테이블 생성시 해당 칼럼은 제외됨으로 생성 후에도 해당 칼럼이 테이블에 없어야 함
 */
@Data
// @Entity
@Table(name="Employee6", uniqueConstraints={@UniqueConstraint(columnNames={"NAME", "MAILID"})})
public class Employee6 {

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

    @Transient
	private String searchCondition;
	
	@Transient
	private String searchKeyword;
}
