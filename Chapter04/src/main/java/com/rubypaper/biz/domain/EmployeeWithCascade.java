package com.rubypaper.biz.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/** 영속성 전이 테스트용 엔티티 */
@Data
@Entity
@Table(name = "S_EMP")
public class EmployeeWithCascade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 25, nullable = false)
	private String name;
	
	// 다대일 관계(다: 사원, 일: 부서, 여러명의 사원이 한 부서에 속함)
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name = "DEPT_ID")
	private DepartmentWithCascade dept;
	
	// 4. 사원 등록과 동시에 부서 등록
	// 	  -> 양방향 참조 관계가 자동으로 설정됨!
	public void setDept(DepartmentWithCascade department) {
		this.dept = department;
		
		/*
		 * null 처리
		 * 
		 * 멤버변수 dept 를 null 로 초기화
		 * -> 현재 엔티티 사원은 어떤 부서에도 소속되는게 아닌 상태가 됨
		 * 
		 * 그러나, 매개변수가 null 이면 해당 메서드는 nullPointException 이 발생하게 될 것.
		 * 이를 위한 체크
		 */

		// 방법1. setDept에 null 체크 추가
		if(department != null) {
			// Department 엔티티의 컬렉션에도 Employee 참조를 설정한다.
			// 부서 정보가 할당되었다는 것은 부서 배치가 끝났음을 의미
			// 따라서, 이 부서에서 현재 엔티티의 사원이 소속된다는 말과 동일함
			department.getEmployeeList().add(this);
		}
	}
	
	// 부서 정보를 null 로 설정하여 직원을 대기 상태로 전환
	// 방법2. 메서드 추가하여 null 처리
	public void standby() {
		this.dept = null;
	}
}
