package com.rubypaper.biz.domain;

import javax.persistence.*;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "dept")
@Entity
@Table(name = "S_EMP")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(name = "MAIL_ID")
	private String mailId;
	
	private Double salary;

	/* @ManyToOne
	 *
	 * - fetch 의 기본 속성값 : EAGER.
	 *   One 에 해당하는 데이터는 항상 EAGER 로 해도 크게 문제가 되지 않으므로
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DEPT_ID")
	private Department dept;

	public void setDept(Department department) {
		this.dept = department;

		// Department 엔티티 컬렉션에도 Employee 참조 설정
		department.getEmployeeList().add(this);
	}
	
}
