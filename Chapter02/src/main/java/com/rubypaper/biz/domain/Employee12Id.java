package com.rubypaper.biz.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 복합 키 매핑을 위한 식별자 클래스
 * 
 * 복합키 후보군 
 * 	- id, mailId        -> 교재 선정
 *  - id, name
 *  - id, mailId, name
 * 
 * 식별자 클래스 작성 규칙 
 * p.140
 * 	- @Embeddable
 * 	- java.io.Serializable 구현
 * 	- 기본 생성자 및 모든 멤버를 초기화하는 생성자 선언
 * 	- 복합 키에 해당하는 변수들만 선언
 * 	- 값을 임의로 변경할 수 없도록 Getter 메서드만 제공
 * 	- equals, hashCode 재정의 
 */
@Embeddable		// 식별자 클래스로 사용되는 클래스임을 명시
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Employee12Id implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String mailId;
}
