package com.rubypaper.biz.client;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class CriteriaSearchOrderClient {

    /** ORDER BY 절
     * 
     * CriteriaQuery.orderBy
     * 
     * 부서이름을 기준으로 내림차순으로 정렬
     * 
     * 쿼리 >>
     * SELECT 
     * FROM Employee emp
     * ORDER BY emp.dept.name DESC
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
     * order by
     *     department1_.name desc
     */
    @Test
    public void shouldSelectUseGroupingHaving() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
        EntityManager em = emf.createEntityManager();

        try {
            
            dataInsert(emf);

            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = builder.createQuery(Employee.class);

            // FROM Employee emp
            Root<Employee> emp = cq.from(Employee.class);

            // SELECT emp
            cq.select(emp);

            // JOIN FETCH emp.dept dept
            // JOIN FETCH 를 안써주면?
            //  총 3번의 쿼리가 날라감
            //  1. cross join 이 실행   -> Employee 엔티티 정보 저장
            //  2. 부서 테이블에 존재하는 DEPT_ID 만큼 조회, Department 엔티티 정보 저장 (2번 실행)
            emp.fetch("dept");

            // ORDER BY emp.dept.name DESC
            cq.orderBy(builder.desc(emp.get("dept").get("name")));

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

    /** Order By 조건이 2개 이상인 경우 */
    @Test
    public void shouldSelectUseGroupings() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
        EntityManager em = emf.createEntityManager();

        try {
            
            dataInsert(emf);

            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = builder.createQuery(Employee.class);

            // FROM Employee emp
            Root<Employee> emp = cq.from(Employee.class);

            // SELECT emp
            cq.select(emp);

            // JOIN FETCH emp.dept dept
            emp.fetch("dept");

            // ORDER BY emp.dept.name DESC, emp.salary DESC
            Order[] orderList = {builder.desc(emp.get("dept").get("name")),
                                builder.desc(emp.get("salary"))};

            cq.orderBy(orderList);

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
