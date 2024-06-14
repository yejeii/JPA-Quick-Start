package com.rubypaper.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.rubypaper.persistence.mybatis.EmployeeVO;

public class EmployeeDAO {

	// JDBC 변수 선언
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	// S_EMP 테이블 관련 SQL 구문
	private String INSERT_EMP = 
			"INSERT INTO s_emp(id, name, start_date, title, dept_name, salary) " +
			"VALUES( ( SELECT nvl(max(id), 0) + 1 FROM s_emp ), ?, ?, ?, ?, ? )";
	private String LIST_EMP = 
			"SELECT * FROM s_emp ORDER BY name";
	
	// 회원 등록 기능
	public void insertEmployee(EmployeeVO vo) {
		System.out.println("===> JDBC 기반으로 직원 등록 기능 처리");
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(INSERT_EMP);
			pstmt.setString(1, vo.getName());
			pstmt.setTimestamp(2, vo.getStartDate());
			pstmt.setString(3, vo.getTitle());
			pstmt.setString(4, vo.getDeptName());
			pstmt.setDouble(5, vo.getSalary());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(pstmt, conn);
		}
	}
	
	// 회원 목록 조회 기능
	public List<EmployeeVO> getEmployeeList() {
		System.out.println("===> JDBC 기반으로 직원 목록 조회 기능 처리");
		List<EmployeeVO> employeeList = new ArrayList<EmployeeVO>();
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(LIST_EMP);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				EmployeeVO employee = new EmployeeVO();
				employee.setId(rs.getLong("ID"));
				employee.setName(rs.getString("NAME"));
				employee.setStartDate(rs.getTimestamp("START_DATE"));
				employee.setTitle(rs.getString("TITLE"));
				employee.setDeptName(rs.getString("DEPT_NAME"));
				employee.setSalary(rs.getDouble("SALARY"));
				employeeList.add(employee);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, pstmt, conn);
		}
		return employeeList;
	}



	// 커넥션 획득
	private Connection getConnection() {
		try {
			Class.forName("org.h2.Driver");
			String url = "jdbc:h2:tcp://localhost/~/test";
			conn = DriverManager.getConnection(url, "sa", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	// 커넥션 종료(ResultSet, Statement, Connection)
	private void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {
			if(rs != null) rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs = null;
		}
		
		try {
			if(pstmt != null) pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
		}
		
		try {
			if(conn != null && !conn.isClosed()) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn = null;
		}
		
	}
	
	// 커넥션 종료(Statement, Connection)
	private void close(PreparedStatement pstmt, Connection conn) {
		try {
			if(pstmt != null) pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pstmt = null;
		}
		
		try {
			if(conn != null && !conn.isClosed()) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn = null;
		}
	}
}
