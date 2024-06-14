package com.rubypaper.biz.domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"searchCondition", "searchKeyword"})
@Entity
@Table(name = "S_EMP")
//@TableGenerator(name="SEQ_GENERATOR"
//				, table = "SHOPPING_SEQUENCES"
//				, pkColumnName = "SEQ_NAME"
//				, pkColumnValue = "EMP_SEQ"
//				, valueColumnName = "NEXT_VALUE"
//				, initialValue = 0
//				, allocationSize = 1)
//@SequenceGenerator(name = "S_EMP_GENERATOR",
//					sequenceName = "S_EMP_SEQUENCE",
//					initialValue = 1,
//					allocationSize = 50)
@Access(AccessType.FIELD)
public class Employee {

	@Id
	@Column(length = 7, nullable = false)
//	private Long id;
	private EmployeeId empId;
	
	@Column(length = 25, nullable = false)
	private String name;
	
//	@Column(length = 8, unique = true)
//	private String mailId;
	
	@Column(name = "START_DATE")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(length = 25)
	private String title;

	@Column(name = "DEPT_NAME", length = 30)
	private String deptName;
	
	@Column(precision = 11, scale = 2)
	private Double salary;

	@Column(name = "COMMISSION_PCT", precision = 4, scale = 2, 
			columnDefinition = "double CHECK(commission_pct IN(10, 12.5, 15, 17.5, 20))")
	private Double commissionPct;
	
	@Transient
	private String searchCondition;
	
	@Transient
	private String searchKeyword;
	
}
