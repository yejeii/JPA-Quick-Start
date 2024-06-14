package com.rubypaper;

import java.sql.Timestamp;
import java.util.List;

import com.rubypaper.persistence.hibernate.EmployeeVO;
import com.rubypaper.persistence.hibernate.EmployeeDAO;

public class EmployeeClientByHibernateTest {

	public static void main(String[] args) {
		EmployeeVO vo = new EmployeeVO();
		vo.setId(6L);
		vo.setName("신길림");
		vo.setStartDate(new Timestamp(System.currentTimeMillis()));
		vo.setTitle("과장");
		vo.setDeptName("총무부");
		vo.setSalary(2900.00);
		vo.setEmail("gillim@ruby.co.kr");
		
		EmployeeDAO employeeDAO = new EmployeeDAO();
		employeeDAO.insertEmployee(vo);
		
		List<EmployeeVO> employeeList = employeeDAO.getEmployeeList();
		for(EmployeeVO employee : employeeList) {
			System.out.println("---> " + employee.toString());
		}

	}

}
