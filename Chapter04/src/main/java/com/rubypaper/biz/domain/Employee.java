package com.rubypaper.biz.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "S_EMP")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 25, nullable = false)
	private String name;
	
	// 다대일 관계(다: 사원, 일: 부서, 여러명의 사원이 한 부서에 속함)
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name = "DEPT_ID")
	private Department dept;
	
	// 4. 사원 등록과 동시에 부서 등록
	// 	  -> 양방향 참조 관계가 자동으로 설정됨!
	 public void setDept(Department department) {
		 this.dept = department;

		// Department 엔티티의 컬렉션에도 Employee 참조를 설정한다.
		// 부서 정보가 할당되었다는 것은 부서 배치가 끝났음을 의미
		// 따라서, 이 부서에서 현재 엔티티의 사원이 소속된다는 말과 동일함
		 department.getEmployeeList().add(this);
	 }
	
}
