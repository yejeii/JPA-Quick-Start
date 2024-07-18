package com.rubypaper.biz.domain;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
// @EqualsAndHashCode(exclude = "employeeList")	// Set 으로 인한 lombok 순환참조 문제 해결
@Entity
@Table(name = "S_DEPT")
public class Department {
	
	@Id
	@Column(name="DEPT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deptId;
	
	@Column(length = 25, nullable = false)
	private String name;
	
	/*
	 * 지금까지는 단방향( Employee -> Department )
	 * 앞으로는 양방향을 위해서 새로운 연결점(방향성) 추가 : ( Department -> Employee )
	 * -> Department class 에 Employee class 를 참조할 수 있는 멤버 변수 추가
	 * -> 관계설정은? 하나의 부서에 여러 사원이 속함 ( 1: N 관계 )
	 * -> 멤버변수의 형태는? 여러 명의 사원정보를 관리 -> List 형태의 자료구조가 필요
	 * 
	 * mappedBy : 연관관계 소유자 속성
	 * -> 연관관계 : S_EMP 와 S_DEPT 테이블의 관계는 FK 로 설정되어 있음
	 * -> dept 는 FK 정보인데, 현재 FK 는 Employee.dept 에 설정되어 있음
	 * -> 그래서, mappedBy 에 FK 관계정보인 Employee 의 dept 멤버로 설정 (Entity 기준) 
	 * 
	 * fetch=FetchType.LAZY
	 * 도메인상 부서정보를 관리하는 엔티티이고, 양방향으로 설정됨
	 * - 활용(검색)측면
	 * 	 1. 부서정보만 활용
	 *      도메인상 부서정보만 집중해서 관리하므로 당연히 1번 활용도가 높다는 것을 알 수 있음
	 *   
	 *   2. 부서정보 + 사원 정보
	 *      기본적으로 부서 정보를 사용하지만, 필요에 따라 실질적으로 사원정보가 필요할 떄만 SELECT 가 발생하면 됨
	 * 
	 *   3. 예외
	 *      무조건 부서정보와 사원정보를 함께 조회해야 하는 경우
	 *      선택적으로 FetchType.EAGER 를 사용
	 */
	@OneToMany(mappedBy = "dept", fetch=FetchType.EAGER)
	private List<Employee> employeeList = new ArrayList<Employee>();
}
