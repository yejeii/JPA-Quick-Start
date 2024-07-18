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
import javax.persistence.criteria.Root;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class CriteriaSearchGroupingClient {

    /** GROUP BY , HAVING 절 추가
     * 
     * CriteriaQuery.groupBy, CriteriaQuery.having
     * 
     * 부서이름별로 급여 총합, 인원 수, 평균 급여를 조회하되, 
     * 인원이 3명 이상인 부서에 한정
     * 
     * 쿼리 >>
     * SELECT emp.dept.name, SUM(emp.salary), COUNT(emp), AVG(emp.salary)
     * FROM Employee emp
     * GROUP By emp.dept.name
     * HAVING COUNT(emp) >= 3
     * 
     * 결과 >>
     * Hibernate: 
     * select
     *     department1_.name as col_0_0_,
     *     sum(employee0_.salary) as col_1_0_,
     *     count(employee0_.id) as col_2_0_,
     *     avg(employee0_.salary) as col_3_0_ 
     * from
     *     S_EMP employee0_ cross 
     * join
     *     S_DEPT department1_ 
     * where
     *     employee0_.DEPT_ID=department1_.DEPT_ID 
     * group by
     *     department1_.name
     * having
     *     count(employee0_.id)>=3
     */
    @Test
    public void shouldSelectUseGroupingHaving() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
        EntityManager em = emf.createEntityManager();

		try {

            dataInsert(emf);

            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Object[]> cq = builder.createQuery(Object[].class);

            // FROM Employee emp
            Root<Employee> emp = cq.from(Employee.class);

            // SELECT SUM(emp.salary), COUNT(emp.name), AVG(emp.salary)
            cq.multiselect(emp.<String>get("dept").get("name"),
                        builder.sum(emp.<Double>get("salary")),
                        builder.count(emp),
                        builder.avg(emp.<Double>get("salary")));

            // GROUP BY emp.dept
            cq.groupBy(emp.get("dept").get("name"));

            // HAVING COUNT(emp) >= 3
            cq.having(builder.ge(builder.count(emp), 3));

            TypedQuery<Object[]> query = em.createQuery(cq);
            List<Object[]> resultList = query.getResultList();
            for (Object[] result : resultList) {
                System.out.println("---> " + Arrays.toString(result));
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
