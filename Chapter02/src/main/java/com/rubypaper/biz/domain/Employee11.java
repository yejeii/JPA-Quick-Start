package com.rubypaper.biz.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name="Employee11")
public class Employee11 {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	private String mailId;
	
}
