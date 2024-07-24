package com.rubypaper.biz;

import com.rubypaper.biz.config.SpringConfiguration;
import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.service.DepartmentService;
import com.rubypaper.biz.service.EmployeeService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;

import java.util.List;

/** p.549 쿼리 메서드 사용 */
public class QueryMethodClient {

    @Test
    public void run() {
        AnnotationConfigApplicationContext container =
                new AnnotationConfigApplicationContext(SpringConfiguration.class);

        DepartmentService dService = (DepartmentService) container.getBean("deptService");
        EmployeeService eService = (EmployeeService) container.getBean("empService");

        dataInsert(dService, eService);
        dataSelect(eService);

        container.close();
    }

    private void dataSelect(EmployeeService eService) {
        Employee employee = new Employee();
        employee.setName("");
        employee.setMailId("Dev");
        Page<Employee> pageInfo = eService.getEmployeeList(employee, 2);

        System.out.println("한 페이지에 출력되는 데이터 수 : " + pageInfo.getSize());
        System.out.println("전체 페이지 수 : " + pageInfo.getTotalPages());
        System.out.println("전체 데이터 수 : " + pageInfo.getTotalElements());

        if(pageInfo.hasPrevious()) {
            System.out.println("이전 페이지 : " + pageInfo.previousPageable());
        } else {
            System.out.println("첫 번째 페이지입니다.");
        }

        if(pageInfo.hasNext()) {
            System.out.println("다음 페이지 : " + pageInfo.nextPageable());
        } else {
            System.out.println("마지막 페이지입니다.");
        }

        List<Employee> resultList = pageInfo.getContent();
        System.out.println("\n [ 검색된 사원 목록 ]");
        for (Employee result : resultList) {
            System.out.println("---> " + result.toString());
        }
    }

    private void dataInsert(DepartmentService dService, EmployeeService eService) {

        Department department1 = new Department();
        department1.setName("개발부");
        dService.insertDepartment(department1);

        Department department2 = new Department();
        department2.setName("영업부");
        dService.insertDepartment(department2);

        for (int i = 1; i <= 5; i++) {
            Employee employee = new Employee();
            employee.setName("개발직원 " + i);
            employee.setSalary(i * 12700.00);
            employee.setMailId("Dev -" + i);
            employee.setDept(department1);
            eService.insertEmployee(employee);
        }

        for (int i = 1; i <= 7; i++) {
            Employee employee = new Employee();
            employee.setName("영업직원 " + i);
            employee.setSalary(i * 24300.00);
            employee.setMailId("Sales -" + i);
            employee.setDept(department2);
            eService.insertEmployee(employee);
        }

    }


}
