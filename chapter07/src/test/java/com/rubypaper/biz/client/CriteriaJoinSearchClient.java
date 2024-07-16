package com.rubypaper.biz.client;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class CriteriaJoinSearchClient {

    @Test
    public void run() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
		try {
			dataInsert(emf);
			dataSelectUseCriteriaExplicitJoinFromDept(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	} 

    /**
     * Criteria + 묵시적(Inplicit) 조인
     * 
     * 연관관계에 있는 객체 변수를 사용하면 내부적으로 묵시적 조인이 동작
     * 
     * Hibernate >>
     *  select
     *      employee0_.name as col_0_0_,
     *      employee0_.salary as col_1_0_,
     *      department1_.name as col_2_0_ 
     *  from
     *      S_EMP employee0_ cross 
     *  join
     *      S_DEPT department1_ 
     *  where
     *      employee0_.DEPT_ID=department1_.DEPT_ID
     * 
     * -> 부서 정보가 없는 직원 정보 출력 X
     */
    public void datatSelectUseCriteriaInplicitJoin(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);

        // FROM Employee emp
        Root<Employee> emp = cq.from(Employee.class);

        // SELECT emp.name, emp.salary, emp.dept.name
        cq.multiselect(
            emp.get("name"),
            emp.get("salary"),
            emp.get("dept").get("name") // 부서 이름(emp.dept.name)
        );

        TypedQuery<Object[]> query = em.createQuery(cq);
        List<Object[]> resultList = query.getResultList();
        for (Object[] result : resultList) {
            System.out.println("---> " + Arrays.toString(result));
        }

        em.close();
    }

    /**
     * Criteria + 명시적(Explicit) 조인
     * 
     * javax.persistence.criteria.Join API 사용
     * join() : 참조 변수를 통해 연관관계에 있는 객체를 명시적으로 조인할 때 사용
     * 
     * Join<Z, X> 
     *  - Z : 검색 대상이 되는 소스 엔티티 지정
     *  - X : 조인 대상이 되는 타켓 엔티티  ( 참조되는 대상 )
     * 
     * Hibernate >>
     * select
     *     employee0_.name as col_0_0_,
     *     employee0_.salary as col_1_0_,
     *     department1_.name as col_2_0_ 
     * from
     *     S_EMP employee0_ 
     * inner join
     *     S_DEPT department1_ 
     *         on employee0_.DEPT_ID=department1_.DEPT_ID
     * 
     * -> 기본적으로 내부 조인으로 처리되므로 부서 정보가 없는 직원 정보는 출력 X
     * -> 부서에 속하지 않는 직원(아르바이트)까지 조회 원하면, JoinType 을 LEFT 로 지정!
     * [아르바이트, 10000.0, null]
     */
    public void dataSelectUseCriteriaExplicitJoin(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);

        // FROM Employee emp
        Root<Employee> emp = cq.from(Employee.class);

        // INNER JOIN emp.dept dept
        // Join<Employee, Department> dept = emp.join("dept");

        // 부서가 없는 직원까지 조회
        // LEFT OUTER JOIN emp.dept dept
        Join<Employee, Department> dept = emp.join("dept", JoinType.LEFT);

        // SELECT emp.name, emp.salary, dept.name
        cq.multiselect(
            emp.get("name"),    // 직원 이름
            emp.get("salary"),  // 직원 급여
            dept.get("name")    // 부서 이름
        );

        TypedQuery<Object[]> query = em.createQuery(cq);
        List<Object[]> resultList = query.getResultList();
        for (Object[] result : resultList) {
            System.out.println("---> " + Arrays.toString(result));
        }

        em.close();
    }

    /** 
     * 부서 정보를 통한 직원 목록 출력 
     * 
     * Hibernate: 
     * select
     *     department0_.name as col_0_0_,
     *     employeeli1_.name as col_1_0_ 
     * from
     *     S_DEPT department0_ 
     * inner join
     *     S_EMP employeeli1_ 
     *         on department0_.DEPT_ID=employeeli1_.DEPT_ID
     * */
    public void dataSelectUseCriteriaExplicitJoinFromDept(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);

        // FROM Department dept
        Root<Department> dept = cq.from(Department.class);

        // INNER JOIN
        Join<Department, Employee> emp = dept.join("employeeList");

        // SELECT dept.name, emp.name
        cq.multiselect(
            dept.get("name"),
            emp.get("name")
        );

        TypedQuery<Object[]> query = em.createQuery(cq);
        List<Object[]> resultList = query.getResultList();
        for (Object[] result : resultList) {
            System.out.println("---> " + Arrays.toString(result));
        }

        em.close();
    }

    public void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

        // 부서 정보 등록
        Department devDept = new Department();
        devDept.setName("개발부");
        em.persist(devDept);

        Department salseDept = new Department();
        salseDept.setName("영업부");
        em.persist(salseDept);
		
		// 개발부에 3 명의 직원 정보 등록
		for(int i=1; i<=3; i++) {
			Employee employee = new Employee();
			employee.setName("개발맨" + i);
			employee.setMailId("Corona" + i);
            // employee.setDeptName("개발부"); 
            employee.setDept(devDept);
			employee.setSalary(12700.00 * i);
			employee.setStartDate(new Date());
			employee.setTitle("사원");
			employee.setCommissionPct(10.00);
			em.persist(employee);
		}
		
		// 영업부에 3 명의 직원 정보 등록
        for (int i = 1; i <= 3; i++) {
            Employee employee = new Employee();
            employee.setName("영업맨"+i);
            employee.setMailId("Virus"+i); 
            // employee.setDeptName("영업부"); 
            employee.setDept(salseDept);
            employee.setSalary(23800.00*i);
            employee.setStartDate(new Date());
            employee.setTitle("과장");  
            employee.setCommissionPct(15.00);
			em.persist(employee);
        }

		// 부서가 없는 직원 등록
		Employee employee = new Employee();
		employee.setName("아르바이트");
		employee.setMailId("Alba-01"); 
		employee.setSalary(10000.00);
		em.persist(employee);

		em.getTransaction().commit();
		em.close();
	}
}

