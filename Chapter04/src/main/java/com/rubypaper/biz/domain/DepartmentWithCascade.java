package com.rubypaper.biz.domain;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

/** 영속성 전이 테스트용 엔티티 */
@Data
@ToString(exclude="employeeList")
// @EqualsAndHashCode(exclude = "employeeList")	// Set 으로 인한 lombok 순환참조 문제 해결
@Entity
@Table(name = "S_DEPT")
public class DepartmentWithCascade {
	
	@Id
	@Column(name="DEPT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deptId;
	
	@Column(length = 25, nullable = false)
	private String name;
	
	/** 양방향 매핑에서 영속성 전이 설정
	 * 
	 * {CascadeType.PERSIST} : 해당 부서 등록 시, 연관관계에 있는 사원 정보 함께 등록
	 * {CascadeType.REMOVE} : 해당 부서 삭제 시, 연관관계에 있는 사원 정보 함께 삭제
	 */
	//@OneToMany(mappedBy = "dept")
	//  @OneToMany(mappedBy = "dept", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
	// @OneToMany(mappedBy = "dept", cascade={CascadeType.PERSIST}, orphanRemoval=true)	// 고아 객체 제거 속성
	@OneToMany(mappedBy = "dept", cascade={CascadeType.PERSIST})	// 고아 객체 null 로 설정
	private List<EmployeeWithCascade> employeeList = new ArrayList<EmployeeWithCascade>();

}
