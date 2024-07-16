package com.rubypaper.biz.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 식별자
 * 
 * p.138
 * 
 * 복합키 식별자 클래스를 활용한 엔티티 작성
 * 복합키 식별자 값을 클라이언트에서 직접 생성하여 할당
 * 
 * Employee12 : 엔티티 클래스
 * Employee12Id : 식별자 클래스
 *                복합키에 해당하는 칼럼인 mailId, name 인 두 개의 멤버로 구성
 * 
 *                pk : id ( 식별자 )
 *                unique Constraints : mailId, name ( 식별자 X . 추가적인 unique 조건일 뿐 )
 * 
 *                복합키 : 식별자가 될 수 있는 복합키여야 함
 *                         -> @ID 가 설정된 식별자 변수와 함께 해야 함
 *                         -> 식별자 클래스의 복합키에 대한 후보군
 *                            id, mailId        -> 교재 선정
 *                            id, name
 *                            id, mailId, name
 * 
 *                식별자 클래스 Emploee12Id 객체를 생성한 후, Employee12 타입의 인스턴스 변수를 선언
 * 
 * Employee12ServiceTest
 */
@Data
@Entity
@Table(name="Employee12")
public class Employee12 {

    @Id
	// private Long id;         // 제거
    private Employee12Id empId;
	
	private String name;

    // private String mailId;   // 제거
	
}
