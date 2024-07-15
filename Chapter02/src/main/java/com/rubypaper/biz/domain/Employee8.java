package com.rubypaper.biz.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

/**
 * 식별자
 * 
 * JPA 가 관리하는 엔티티는 @Id 로 지정한 식별자 변수를 통해 식별됨
 * 테이블의 기본키와 엔티티의 식별자 변수를 매핑해 유일한 엔티티 객체를 식별하고 관리
 * 
 * 식별자 생성 전략( 중요 )
 * p.118 참고
 * 
 * 1. Identity 전략
 * @GeneratedValue(strategy=GenerationType.IDENTITY)
 */
@Data
@Entity
@Table(name="Employee8")
@ToString
public class Employee8 {
	
	@Id// S_EMP 테이블의 PK 와 매핑
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String mailId;
}