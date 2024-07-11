package com.rubypaper.biz.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;

public class JPQLGroupAndPaging {

    @Test
    public void executeJPQL() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter06");
		try {
			dataInsert(emf);
			dataSelectPaging(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

    private void dataSelectByGroupFunc(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();

        // JPQL
        String jpql = "SELECT d.name, MAX(e.salary), MIN(e.salary), SUM(e.salary), COUNT(e.salary), AVG(e.salary) "
                + "FROM Employee e JOIN e.dept d "
                + "GROUP BY d.name "
                + "HAVING AVG(e.salary) >= 30000.0";
        TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);

        List<Object[]> resultList = query.getResultList();
        System.out.println("부서 별 급여 정보");
        for(Object[] result : resultList) {
            String deptName = (String) result[0];
            Double max = (Double) result[1];
            Double min = (Double) result[2];
            Double sum = (Double) result[3];
            Long count = (Long) result[4];
            Double avg = (Double) result[5];

            System.out.printf("%s :  ", deptName);
            System.out.printf("MAX : %7.2f ", max);
            System.out.printf("MIN : %7.2f ", min);
            System.out.printf("SUM : %7.2f ", sum);
            System.out.printf("COUNT : %d ", count);
            System.out.printf("AVG : %7.2f%n", avg);
        }
        em.close();
    }

	private void dataSelectPaging(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// 번외.
		// 조인 없이 진행 + DEPT 정보 함께 가져올 때 DEPT_ID null 인 외딴 직원에 대한 nullpointerException 주의!
		// String jpql = "SELECT e FROM Employee e ORDER BY e.id";
		// TypedQuery<Employee> query = em.createQuery(jpql, Employee.class);
		
		String jpql = "SELECT e, e.dept FROM Employee e ORDER BY e.id";
		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);

		int pageNumber = 2;	// 해당 페이지 번호
		int pageSize = 5;	// 한 페이지당 표시 행 개수s
		int startNum = (pageNumber * pageSize) - pageSize;	// 보여줄 행 번호 시작점
		query.setFirstResult(startNum);
		query.setMaxResults(pageSize);	// == limit(,)

		// System.out.println(pageNumber + "페이지에 해당하는 직원 목록");
		// List<Employee> resultList = query.getResultList();
		// for(Employee employee : resultList) {
		// 	System.out.println(employee.getId() + " : " + employee.getName());
		// }

		List<Object[]> resultList = query.getResultList();
		System.out.println(pageNumber + "페이지에 해당하는 직원 목록");
		for(Object[] result : resultList) {
			Employee employee = (Employee) result[0];
			Department department = (Department) result[1];
			System.out.println(employee.getId() + " : " + department.getName() + "의 부서 " + department.getName());
		}

		em.close();
	}

    private void dataInsert(EntityManagerFactory emf) {

		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Department department1 = new Department();
		department1.setName("개발부");
		
		for(int i=1; i<=3; i++) {
			Employee employee = new Employee();
			employee.setName("개발세발직원" + i);
			employee.setMailId("Dev-" + i);
			employee.setSalary(12700.00 * i);
			employee.setDept(department1);
		}
		em.persist(department1);
		
		Department department2 = new Department();
		department2.setName("영업부");
		
		for(int i=1; i<=4; i++) {
			Employee employee = new Employee();
			employee.setName("영업직원" + i);
			employee.setMailId("Sale-" + i);
			employee.setSalary(27300.00 * i);
			employee.setDept(department2);
		}
		em.persist(department2);
		
		Department department3 = new Department();
		department3.setName("인재개발부");
		em.persist(department3);
		
		// Outer Join 을 위한 추가 : 부서 정보가 없는 새로운 직원 추가
		Employee employee = new Employee();
		employee.setName("아르바이트");
		employee.setMailId("Alba-01");
		employee.setSalary(10000.00);
		em.persist(employee);
		
		// Theta Join 을 위한 추가 : 이름이 영업부인 새로운 직원 추가
		Employee employee2 = new Employee();
		employee2.setName("영업부");
		employee2.setSalary(11850.00);
		em.persist(employee2);
		
		em.getTransaction().commit();
		em.close();
	}
}
