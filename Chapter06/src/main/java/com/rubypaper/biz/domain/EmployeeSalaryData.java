package com.rubypaper.biz.domain;

import lombok.AllArgsConstructor;

import lombok.Data;

// 직원 급여 관련 정보 테이블
@Data
@AllArgsConstructor
public class EmployeeSalaryData {

	private Long id;
	private Double salary;
	private Double commissionPct;
}
