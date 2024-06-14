package com.rubypaper.persistence.mybatis;

import java.sql.Timestamp;

import lombok.Data;
import lombok.ToString;

@Data
public class EmployeeVO {
	private Long id;
	private String name;
	private Timestamp startDate;
	private String title;
	private String deptName;
	private Double salary;
	
}
