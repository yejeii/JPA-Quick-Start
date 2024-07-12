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

import com.rubypaper.biz.domain.Employee;

public class CriteriaSearchClient {

    @Test
    public void run() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter07");
		try {
			dataInsert(emf);
			dataSelect(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}    

	/**
	 * 검색 조건에 따른 분기 처리 
	 * -> 검색 조건에 달라짐(요구사항)에 따라 메서드를 수정해야 하는 문제 발생
	 * -> OCP 위배..
	 * 
	 * -> 크라이테리어로 해결!
	 */
	private static void dataSelect(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// 검색 정보 설정
		String searchCondition = "NAME";
		String searchKeyword = "아르바이트";

		// 검색 관련 쿼리
		String jpqlByMailId = "SELECT e FROM Employee e WHERE e.mailId = :searchKeyword";
		String jpqlByName = "SELECT e FROM Employee e WHERE e.name = :searchKeyword";

		TypedQuery<Employee> query = null;

		// 검색 조건에 따른 분기 처리
		if(searchCondition.equals("NAME")) {
			query = em.createQuery(jpqlByName, Employee.class);
		} else if(searchCondition.equals("MAILID")) {
			query = em.createQuery(jpqlByMailId, Employee.class);
		}

		query.setParameter("searchKeyword", searchKeyword);
		List<Employee> resultList = query.getResultList();

		System.out.println(searchCondition + "을 기준으로 검색 결과");
		for (Employee result : resultList) {
			System.out.println("---> " + result.toString());		
		}

		em.close();
	}

	/**
	 * Criteria를 통한 프로그램에서 중복되는 쿼리 최소화
	 * 
	 * Criteria 
	 * 	- JPQL을 자바 코드로 작성하도록 도와주는 빌더 클래스 API
	 * 	  -> 빌더 패턴을 이용해 메시지 체인을 이용해 점진적으로 객체를 완성해 나갈 수 있음
	 * 	  -> 필요시, where() 를 맘대로 구성할 수 있음
	 * 	  
	 * 	  -> 그러나,, 이것도 완전한 OCP 해결이라고는 말 못함...
	 *    -> 조건에 따라 else if 문을 추가해야 하기 때문.. 
	 * 
	 * 	- 사용 방법
	 * 		1. Criteria 빌더에서 Criteria 쿼리(CriteriaQuery) 생성
	 *         	- CriteriaQuery 클래스에서 제공하는 select, from, where, groupBy, having, orderBy 메서드를 통해
	 * 			  동적인 JPQL 객체로 점진적으로 완성시킬 수 있음
	 * 		2.from 절 생성
	 * 			- 반환된 값은 변수에 저장. 이때, 해당 변수명은 Criteria 에서 별칭으로 사용
	 * 			- 조회의 시작점이라는 뜻에서 쿼리 Root 로 사용됨
	 * 		3. select 절 생성
	 * 		4. 검색 조건 정의
	 * 			- 빌더.equal(별칭.get(비교할 대상 컬럼), "사용자가 추가한 검색어")
	 * 		5. 정렬 조건 정의
	 * 		6. 쿼리 생성
	 * 			- 쿼리.select(from 절 변수)
	 * 					.where(where 절 변수)
	 * 					.orderby(orderby 절 변수)
	 * 		7. 결과 받아오기
	 */
	private static void dataSelectUseCriteria(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// 검색 정보 설정
		String searchCondition = "TITLE";
		String searchKeyword = "과장";

		// 1. 크라이테리어 빌더 생성
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// 크라이테리어 쿼리 생성
		CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);

		// 2. from 절 생성 : FROM Employee emp
		Root<Employee> emp = criteriaQuery.from(Employee.class);

		// 3. seleect 절 생성 : SELECT emp
		criteriaQuery.select(emp);

		// 4. 검색 조건에 따른 분기 처리
		// 	  where 절 생성
		if(searchCondition.equals("NAME")) 
		{	
			// WHERE name = :searchKeyword
			// emp.get("name") : Employee 객체의 name 변수 값 리턴
			// emp.get("dept").get("name") : Employee 객체의 dept 변수가 참조하는 부서 객체의 name 변수 값 리턴
			criteriaQuery.where(builder.equal(emp.get("name"), searchKeyword));
		} else if(searchCondition.equals("MAILID")) {
			// WHERE name = :searchKeyword
			criteriaQuery.where(builder.equal(emp.get("mailId"), searchKeyword));
		}

		// 7. 결과 받아오기 
		TypedQuery<Employee> query = em.createQuery(criteriaQuery);
		List<Employee> resultList = query.getResultList();
		for(Employee result: resultList) {
			System.out.println("---> " + result.toString());
		}

		em.close();
	}

	/**
	 * Criteria 사용하여 특정 변수만 선택하여 조회하기 
	 * 
	 * 검색 결과가 엔티티가 아닌 여러 변수로 구성된 경우엔 리턴 타입을 Object[] 사용
	 * 
	 * Criteria 를 이용하여 직원의 아이디, 이름, 급여 정보만 선택적으로 조회
	 */
	private static void dataSelectUseCriteriaReturnObject(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();

		// 1. 크라이테리어 빌더 생성
		CriteriaBuilder builder = em.getCriteriaBuilder();

		// 크라이테리어 쿼리 생성
		CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);	

		// 2. from 절 생성 : FROM Employee emp
		Root<Employee> emp = criteriaQuery.from(Employee.class);

		// 3. select 절 생성
		// 	  SELECT emp.id, emp.name, emp.salary
		criteriaQuery.multiselect(emp.get("id"), emp.get("name"), emp.get("salary"));

		TypedQuery<Object[]> query  = em.createQuery(criteriaQuery);
		List<Object[]> resultList = query.getResultList();
		for (Object[] result : resultList) {
			System.out.println("---> " + Arrays.toString(result));
		}

		em.close();
	}

    private void dataInsert(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 개발부에 3 명의 직원 정보 등록
		for(int i=1; i<=3; i++) {
			Employee employee = new Employee();
			employee.setName("개발맨" + i);
			employee.setMailId("Corona" + i);
			employee.setDeptName("개발부");
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
            employee.setDeptName("영업부"); 
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
