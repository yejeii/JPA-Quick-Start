package com.rubypaper.biz.domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
// @Entity
@Access(AccessType.FIELD)
@ToString(exclude={"searchCondition", "searchKeyword"})
@Table(name="Employee7", uniqueConstraints={@UniqueConstraint(columnNames={"NAME", "MAILID"})})
public class Employee7 {

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
	
    @Column(name = "COMMISSION_PCT", precision = 2, scale = 1, 
			columnDefinition = "double CHECK(commission_pct IN(10, 12.5, 15, 17.5, 20))")
	private Double commisionPct;

    @Transient
	private String searchCondition;
	
	@Transient
	private String searchKeyword;
}

