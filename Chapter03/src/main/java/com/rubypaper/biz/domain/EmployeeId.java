package com.rubypaper.biz.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 복합 키 매핑
 * 
 * - {@Embeddable} 추가 : "해당 클래스는 식별자로 사용되는 클래스이다"
 * - java.io.Serializable 구현
 * - 기본 생성자 및 모든 멤버를 초기화하는 생성자 선언
 * - 복합 키에 해당하는 변수들만 선언
 * - 값을 임의로 변경할 수 없도록 Getter 메서드만 제공
 * - equals, hashCode 재정의 
 * 
 * @author ga29_
 */
//@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class EmployeeId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String mailId;
}
