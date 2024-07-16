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

public class CriteriaFetchJoinSearchClient {

    @Test
    public void run() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
		try {
			dataInsert(emf);
			dataSelectUseCriteriaNonFetch(emf);
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
     * 
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
        for (Department result : resultList) {
            System.out.println(" 부서명 : " + result.getName());
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

