package com.rubypaper.biz.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
// @Entity
@Table(name="Employee9")
@ToString
@SequenceGenerator(name="Employee9_GENERATOR", 
				sequenceName="Employee9_sequence", 	// DB 에 저장된 시퀀스 테이블명
				initialValue=1, 	// sequence 초기값
				allocationSize=50)	// sequence 증가값
public class Employee9 {

    @Id // S_EMP 테이블의 PK 와 매핑
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Employee9_GENERATOR")
	private Long id;
	
	private String name;
	
	private String mailId;
}
