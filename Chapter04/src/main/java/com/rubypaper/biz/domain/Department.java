package com.rubypaper.biz.domain;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = "employeeList")
@Entity
@Table(name = "S_DEPT")
public class Department {
	
	@Id
	@Column(name="DEPT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deptId;
	
	@Column(length = 25, nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "dept", cascade = { CascadeType.PERSIST })
	private List<Employee> employeeList = new ArrayList<Employee>();
}
