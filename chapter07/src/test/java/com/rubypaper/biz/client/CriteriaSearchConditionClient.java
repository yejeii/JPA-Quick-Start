package com.rubypaper.biz.client;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class CriteriaSearchConditionClient {

    /**
     * Employee.dept is null 인 Employee 객체 찾기
     * 
     * 찾을 대상 : 
     * Employee(id=7, name=아르바이트, mailId=Alba-01, startDate=null, title=null, salary=10000.0, commissionPct=null, dept=null)
     */
    @Test
    public void shouldSelectDeptIsNull() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
        EntityManager em = emf.createEntityManager();

		try {
			dataInsert(emf);
			
            CriteriaBuilder builder = emf.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = builder.createQuery(Employee.class);

            // FROM Employee emp
            Root<Employee> emp = cq.from(Employee.class);

            // SELECT emp
            cq.select(emp);

            // JOIN FETCH emp.dept dept
            emp.fetch("dept", JoinType.LEFT);

            // WHERE emp.dept is null
            cq.where(builder.isNull(emp.get("dept")));

            TypedQuery<Employee> query = em.createQuery(cq);
            List<Employee> resultList = query.getResultList();
            for(Employee employee : resultList) {
                System.out.println("---> " + employee.toString());
            }

            em.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	} 

    @Test
    public void shouldSelectEmailIdLike() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
        EntityManager em = emf.createEntityManager();

		try {
			dataInsert(emf);
			
            CriteriaBuilder builder = emf.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = builder.createQuery(Employee.class);

            // FROM Employee emp
            Root<Employee> emp = cq.from(Employee.class);

            // SELECT emp
            cq.select(emp);

            // JOIN FETCH emp.dept dept
            emp.fetch("dept", JoinType.LEFT);

            // WHERE emp.mailId like %rona%
            cq.where(builder.like(emp.<String>get("mailId"), "%rona%"));

            TypedQuery<Employee> query = em.createQuery(cq);
            List<Employee> resultList = query.getResultList();
            for(Employee employee : resultList) {
                System.out.println("---> " + employee.toString());
            }

            em.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    @Test
    public void shouldSelectUseAnd() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
        EntityManager em = emf.createEntityManager();

		try {
			dataInsert(emf);
			
            CriteriaBuilder builder = emf.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = builder.createQuery(Employee.class);

            // FROM Employee emp
            Root<Employee> emp = cq.from(Employee.class);

            // SELECT emp
            cq.select(emp);

            // JOIN FETCH emp.dept dept
            emp.fetch("dept");

            // WHERE emp.dept is not null
            // AND emp.mailId like Viru%
            // AND emp.salary >= 35000.00
            Predicate[] condition = {builder.isNotNull(emp.get("dept")), 
                                    builder.like(emp.<String>get("mailId"), "Viru%"), 
                                    builder.ge(emp.<Double>get("salary"), 35000.00)};
            
            // and() 을 통해 AND 조건으로 연결
            Predicate predicate = builder.and(condition);   
            cq.where(predicate);

            TypedQuery<Employee> query = em.createQuery(cq);
            List<Employee> resultList = query.getResultList();
            for(Employee employee : resultList) {
                System.out.println("---> " + employee.toString());
            }

            em.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    @Test
    public void shouldSelectUseAndOn() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
        EntityManager em = emf.createEntityManager();

		try {
			dataInsert(emf);
			
            CriteriaBuilder builder = emf.getCriteriaBuilder();
            CriteriaQuery<Employee> cq = builder.createQuery(Employee.class);

            // FROM Employee emp
            Root<Employee> emp = cq.from(Employee.class);

            // SELECT emp
            cq.select(emp);

            // JOIN FETCH emp.dept dept
            emp.fetch("dept");

            // WHERE ( emp.mailId like Viru% 
            //      OR emp.salary >= 35000.00 )
            // ADN emp.dept.name = '영업부' )
            Predicate[] condition1 = {builder.like(emp.<String>get("mailId"), "Viru%"), 
                                    builder.ge(emp.<Double>get("salary"), 35000.00)};
            Predicate condition2 = builder.equal(emp.get("dept").get("name"), "영업부");
            
            // or() 을 통해 OR 연산자로 연결
            // and() 을 통해 첫 번째 조건과 두 번째 조건 AND 연산자로 연결
            Predicate predicate = builder.and(builder.or(condition1), condition2);   
            cq.where(predicate);

            TypedQuery<Employee> query = em.createQuery(cq);
            List<Employee> resultList = query.getResultList();
            for(Employee employee : resultList) {
                System.out.println("---> " + employee.toString());
            }

            em.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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

