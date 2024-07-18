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
import javax.persistence.criteria.Subquery;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class CriteriaSearchQueryClient {

    /** 서브쿼리 사용
     * 
     * Subquery 인터페이스
     * 
     * 전체 직원의 평균 급여 이상을 받는 직원 목록 조회
     * 
     * 쿼리 >>
     * SELECT *
     * FROM Employee emp
     * WHERE salary >= ( SELECT AVG(SALARY) FROM Employee )
     * 
     * 결과 >>
     * Hibernate: 
     * select
     *     employee0_.id as id1_1_0_,
     *     department1_.DEPT_ID as DEPT_ID1_0_1_,
     *     employee0_.COMMISSION_PCT as COMMISSI2_1_0_,
     *     employee0_.DEPT_ID as DEPT_ID8_1_0_,
     *     employee0_.MAIL_ID as MAIL_ID3_1_0_,
     *     employee0_.name as name4_1_0_,
     *     employee0_.salary as salary5_1_0_,
     *     employee0_.START_DATE as START_DA6_1_0_,
     *     employee0_.title as title7_1_0_,
     *     department1_.name as name2_0_1_ 
     * from
     *     S_EMP employee0_ 
     * inner join
     *     S_DEPT department1_ 
     *         on employee0_.DEPT_ID=department1_.DEPT_ID 
     * where
     *     employee0_.salary>=(
     *         select
     *             avg(employee2_.salary) 
     *         from
     *             S_EMP employee2_
     *     )
     */
    @Test
    public void shouldSelectUseSubQuery() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
        EntityManager em = emf.createEntityManager();

        try {
            
            dataInsert(emf);
            
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = builder.createQuery(Employee.class);

            /** 서브쿼리 생성 */
            // SELECT AVG(SALARY) FROM Employee
            Subquery<Double> subquery = cq.subquery(Double.class);

            // From Employee emp
            Root<Employee> e = subquery.from(Employee.class);

            // SELECT AVG(SALARY)
            subquery.select(builder.avg(e.<Double>get("salary")));

            /** 메인 쿼리 생성 */
            // From Employee emp
            Root<Employee> emp = cq.from(Employee.class);

            // SELECT emp
            cq.select(emp);

            // JOIN FETCH emp.dept dept
            emp.fetch("dept");

            /** 메인 쿼리에 서브쿼리 연결 */
            // WHERE salary >= (서브쿼리)
            cq.where(builder.ge(emp.<Double>get("salary"), subquery));

            TypedQuery<Employee> query = em.createQuery(cq);
            List<Employee> resultList = query.getResultList();
            for (Employee result : resultList) {
                System.out.println("---> " + result.toString());
            }

            em.close();

        } catch (Exception e) {
        } finally {
            em.close();
            emf.close();
        }
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
