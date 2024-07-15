package com.rubypaper.biz.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Data;

/**
 * 테이블 전략 + 식별자 테이블 생성 
 * @GeneratedValue(strategy=GenerationType.TABLE)
 * @TableGenerator
 * 
 * 식별자 정보를 관리하기 위한 식별자 전용 테이블 생성
 */
@Data
// @Entity
@Table(name="Employee10")
@TableGenerator(name="SEQ_GENERATOR"				// generator 이름
				, table = "SHOPPING_SEQUENCES"		// 테이블명
				, pkColumnName = "SEQ_NAME"			// PK 칼럼명
				, pkColumnValue = "EMP_SEQ"		 	// PK 값
				, valueColumnName = "NEXT_VALUE"	// 식별자 값이 저장되는 칼럼명
				, initialValue = 0					// 초기값
				, allocationSize = 1)				// 증가값
public class Employee10 {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="SEQ_GENERATOR")
	private Long id;
	
	private String name;
	
	private String mailId;
	
}
