package com.rubypaper.biz;

import com.rubypaper.biz.config.SpringConfiguration;
import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.service.DepartmentService;
import com.rubypaper.biz.service.EmployeeService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.List;

public class EmployeeServiceClient {

    @Test
    public void run() {

        /* spring container 생성
         *
         * - 개발자가 객체 생성 및 관리를 다른 누군가에게 위임을 하고 싶음.
         *   => new 연산자를 사용하지 않게 되어, 약결합으로 구현이 가능해짐.
         */
        //GenericXmlApplicationContext container =
        //        new GenericXmlApplicationContext("spring/business-layer.xml");

        // 어노테이션 기반의 스프링 설정
        AnnotationConfigApplicationContext container =
                new AnnotationConfigApplicationContext(SpringConfiguration.class);

        /*
         * spring container 에게 객체 생성을 위임하여, 필요한 객체는 불러서 사용하면 됨.
         */
        DepartmentService dService = (DepartmentService) container.getBean("deptService");
        EmployeeService eService = (EmployeeService) container.getBean("empService");

        dataInsert(dService, eService);
        //dataSelect(eService);
        dataSelect(dService);
        container.close();
    }

    /** 부서를 기준으로 조회
     *
     * 해당 부서에 소속되어 있는 직원 목록 조회
     *
     * 예외 발생 >>
     * org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role:
     * com.rubypaper.biz.domain.Department.employeeList, could not initialize proxy - no Session
     * 즉, 지연 로딩(Lazy Loading)된 엔티티나 컬렉션을 초기화하지 않아서 발생한 예외
     * 다시 말해, 지연 로딩으로 생성된 프록시를 초기화하는 것(즉, 연관된 데이터를 가져오는 것)이 불가능한 상황이 되었다는 의미
     *
     * 조회 결과가 반환되면서 트랜잭션이 이미 종료된 이후에 연관된 데이터에 접근하려 했기 때문
     *
     *
     * 컬렉션용 프록시 : org.hibernate.collection.internal.Persistent*
     * 하이버네이트는 컬렉션을 효율적으로 관리하기 위해 엔티티를 영속 상태로 만들때
     * 원본 컬렉션을 감싸고 있는 내장 컬렉션을 생성해서 내장 컬렉션을 사용하도록 참조를 변경
     *
     * 하이버네이트는 이런 특징 떄문에 컬렉션을 사용할때 즉시 초기화를 권장
     * Collection<Employee> employeeList = new ArrayList<>();
     * */
    private void dataSelect(DepartmentService dService) {
        Department department = new Department();
        department.setDeptID(1L);
        Department findDept = dService.getDepartment(department);

        System.out.println("부서명 : " + findDept.getName());
        System.out.println("직원 엔티티 상태 : " + findDept.getEmployeeList().getClass().getName());
        System.out.println("직원 목록");
        for (Employee employee: findDept.getEmployeeList()) {
            System.out.println("---> " + employee.toString());
        }
    }

    private void dataSingleSelect(EmployeeService eService) {
        Employee employee = new Employee();
        employee.setId(3L);
        Employee findEmp = eService.getEmployee(employee);  // LEFT OUTER JOIN

        System.out.println(findEmp.toString());
    }

    /** 직원을 기준으로 조회
     *
     * 직원 목록과 그에 따른 부서명 조회
     * */
    private void dataSelect(EmployeeService eService) {
        //List<Employee> eList = eService.getEmployeeList(new Employee());

        //System.out.println("직원 목록");
        //for(Employee employee : eList) {
        //    System.out.println("---> " + employee.getName() + "의 부서명 : " + employee.getDept().getName());
        //}
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
