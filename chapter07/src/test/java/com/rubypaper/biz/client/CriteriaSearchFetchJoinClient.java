package com.rubypaper.biz.client;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class CriteriaSearchFetchJoinClient {

    @Test
    public void run() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
		try {
			dataInsert(emf);
			dataSelectUseCriteriaFetch(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	} 

    /** 
     * 페치 조인 사용하지 않고 부서 목록 조회 
     * 
     * Hibernate: 
     * select
     *     department0_.DEPT_ID as DEPT_ID1_0_,
     *     department0_.name as name2_0_ 
     * from
     *     S_DEPT department0_
     * 
     * -> S_DEPT 테이블만 조회
     * -> Department 클래스의 employeeeList 변수에 선언된 @OneToMany 기본 페치 설정이 FetchType.LAZY 이기 때문
     * -> 클라이언트가 부서 정보만 사용하는 경우, S_DEPT 테이블만 조회
     * */
    public void dataSelectUseCriteriaNonFetch(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Department> cq = builder.createQuery(Department.class);

        // FROM Department dept
        Root<Department> dept = cq.from(Department.class);

        // SELECT dept.name, emp.name
        cq.select(dept);

        TypedQuery<Department> query = em.createQuery(cq);
        List<Department> resultList = query.getResultList();
        for (Department department : resultList) {

            // 직원 테이블 조회 X -> FetchType.LAZY
            System.out.println("부서명 : " + department.getName());
        }

        em.close();
    }

    /**
     * 페치 조인 사용하지 않고 부서에 따른 직원 목록 조회
     * 
     * Hibernate: 
     *     select
     *         department0_.DEPT_ID as DEPT_ID1_0_,
     *         department0_.name as name2_0_ 
     *     from
     *         S_DEPT department0_
     * 부서명 : 개발부
     * Hibernate: 
     *     select
     *         employeeli0_.DEPT_ID as DEPT_ID8_1_0_,
     *         employeeli0_.id as id1_1_0_,
     *         employeeli0_.id as id1_1_1_,
     *         employeeli0_.COMMISSION_PCT as COMMISSI2_1_1_,
     *         employeeli0_.DEPT_ID as DEPT_ID8_1_1_,
     *         employeeli0_.MAIL_ID as MAIL_ID3_1_1_,
     *         employeeli0_.name as name4_1_1_,
     *         employeeli0_.salary as salary5_1_1_,
     *         employeeli0_.START_DATE as START_DA6_1_1_,
     *         employeeli0_.title as title7_1_1_ 
     * 
     * -> S_DEPT 테이블 먼저 검색 후, 그에 따른 S_EMP 테이블 검색 추가 실행
     * -> @OneToMany 기본 페치 설정이 FetchType.LAZY 에 의해 지연 로딩된 상태
     * -> 처음부터 조인을 통해 연관관계에 있는 것까지 한 번에 가져올 순 없을까??
     * -> 페치 조인 적용!!
     */
    public void dataSelectUseCriteriaNonFetch2(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Department> cq = builder.createQuery(Department.class);

        // FROM Department dept
        Root<Department> dept = cq.from(Department.class);

        // SELECT dept.name, emp.name
        cq.select(dept);

        TypedQuery<Department> query = em.createQuery(cq);
        List<Department> resultList = query.getResultList();
        for (Department department : resultList) {

            System.out.println("부서명 : " + department.getName());

            // 직원 테이블 조회
            List<Employee> employeeList = department.getEmployeeList();
            for(Employee employee : employeeList) {
                System.out.println(employee.getName());
            }
        }
        em.close();
    }

    /**
     * 페치 조인을 적용하여 연관관계에 있는 객체 한방 탐색
     * 
     * fetch() 
     * 
     * Hibernate: 
     * select
     *     distinct department0_.DEPT_ID as DEPT_ID1_0_0_,
     *     employeeli1_.id as id1_1_1_,
     *     department0_.name as name2_0_0_,
     *     employeeli1_.COMMISSION_PCT as COMMISSI2_1_1_,
     *     employeeli1_.DEPT_ID as DEPT_ID8_1_1_,
     *     employeeli1_.MAIL_ID as MAIL_ID3_1_1_,
     *     employeeli1_.name as name4_1_1_,
     *     employeeli1_.salary as salary5_1_1_,
     *     employeeli1_.START_DATE as START_DA6_1_1_,
     *     employeeli1_.title as title7_1_1_,
     *     employeeli1_.DEPT_ID as DEPT_ID8_1_0__,
     *     employeeli1_.id as id1_1_0__ 
     * from
     *     S_DEPT department0_ 
     * inner join
     *     S_EMP employeeli1_ 
     *         on department0_.DEPT_ID=employeeli1_.DEPT_ID
     */
    public void dataSelectUseCriteriaFetch(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Department> cq = builder.createQuery(Department.class);

        // FROM Department dept
        Root<Department> dept = cq.from(Department.class);

        // JOIN FETCH dept.employeeList
        dept.fetch("employeeList");

        // SELECT DISTINCT dept
        cq.select(dept).distinct(true);

        TypedQuery<Department> query = em.createQuery(cq);
        List<Department> resultList = query.getResultList();
        for (Department department : resultList) {

            System.out.println("부서명 : " + department.getName());

            // 직원 테이블 조회
            List<Employee> employeeList = department.getEmployeeList();
            for(Employee employee : employeeList) {
                System.out.println(employee.getName());
            }
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

