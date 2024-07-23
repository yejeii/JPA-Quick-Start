package com.rubypaper.biz;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.service.DepartmentService;
import com.rubypaper.biz.service.EmployeeService;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

public class EmployeeServiceClient {

    @Test
    public void run() {
        GenericXmlApplicationContext container =
                new GenericXmlApplicationContext("spring/business-layer.xml");

        DepartmentService dService = (DepartmentService) container.getBean("deptService");
        EmployeeService eService = (EmployeeService) container.getBean("empService");

        dataInsert(dService, eService);
        container.close();
    }

    private void dataInsert(DepartmentService dService, EmployeeService eService) {

        Department department1 = new Department();
        department1.setName("개발부");
        dService.insertDepartment(department1);

        Department department2 = new Department();
        department2.setName("영업부");
        dService.insertDepartment(department2);

        for (int i = 0; i <= 5; i++) {
            Employee employee = new Employee();
            employee.setName("개발직원 " + i);
            employee.setSalary(i * 12700.00);
            employee.setMailId("Dev -" + i);
            employee.setDept(department1);
            eService.insertEmployee(employee);
        }

        for (int i = 0; i <= 7; i++) {
            Employee employee = new Employee();
            employee.setName("영업직원 " + i);
            employee.setSalary(i * 24300.00);
            employee.setMailId("Sales -" + i);
            employee.setDept(department2);
            eService.insertEmployee(employee);
        }

    }
}
