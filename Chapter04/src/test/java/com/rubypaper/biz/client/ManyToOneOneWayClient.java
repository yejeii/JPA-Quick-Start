package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.domain.Employee;


/** 단방향 통신 테스트 */
public class ManyToOneOneWayClient {

	@Test
	public void run() {

		// 엔티티 매니저 팩토리 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter04");
		
		try {
			dataInsert(emf);
			// dataSelect(emf);
			// dataUpdate(emf);
			dataDelete(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}
	
	private static void dataInsert(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 부서 등록
		Department department = new Department();
		department.setName("개발부");
		em.persist(department);	
		
		// 직원 등록
		Employee employee1 = new Employee();
		employee1.setName("둘리");
		employee1.setDept(department);
		em.persist(employee1);
		
		// 직원 등록
		Employee employee2 = new Employee();
		employee2.setName("도우너");
		employee2.setDept(department);
		em.persist(employee2);

		// 직원 등록
		// optional=false 로 설정하고 해당 Employee 객체를 persist 하면 예외 발생!
		// Employee employee3 = new Employee();
		// employee3.setName("또돌이");
		// em.persist(employee3);
		
		em.getTransaction().commit();
		em.close();
		
	}

	/** 다대일 관계에서 직원 이름과 부서이름 조회
	 * 
	 * 결과 >>
	 * LEFT OUTER JOIN
	 * 
	 * 왜? >>
	 * 현재의 도메인은 사원관리..
	 * 	- 신규 사원의 경우, 교육이 끝나기 전까지는 부서배치를 보류함
	 * 	  -> 해당 사원의 경우, 부서 정보 X
	 * 
	 * 	- 근무 중에 심각한 문제를 일으킨 경우
	 *    -> 부서에서 제외, 향후에 부서 재배치시킴 
	 *    -> 부서 정보 X
	 * 
	 * 따라서, 기본적으로 전체 사원정보가 조회되도록 하려면, outer join 이 필요하게 됨
	 * 
	 * condition 설정, fetch 설정에 따라 조회 쿼리를 설정할 수 있음
	 * 
	 * 정리 >>
	 * ManyToOne 관계에서 조회를 하는 경우
	 * 
	 * 	1. 기본
	 * 	   outer join
	 * 	2. optional=false
	 * 	   inner join	-> index join(성능이 좋음)
	 * 	3. fetch=LAZY
     * 	   3.1 테이블 하나(S_EMP)로만 조회
	 * 			employee 의 멤버만 사용 -> S_EMP 만 조회하면 가능
	 * 	   3.2 두 테이블(S_EMP, S_DEPT) 모두 조회
	 * 			employee, department 멤버 모두 사용
	 * 			-> S_EMP, S_DEPT 두 테이블을 모두 사용해야만 조회 가능
	 * 	4. fetch=EAGER
	 * 	   기본적으로 두 테이블(S_EMP, S_DEPT) 모두 사용
	 * 	   즉, employee 의 멤버만 사용해도 S_EMP, S_DEPT 두 테이블 모두 사용
	 * 
	 * 성능을 고려한다면..
	 * 	2 > 3 순으로 선택, 이후는 개발 상황에 맞게 적용..
	 */
	private static void dataSelect(EntityManagerFactory emf) {
		
		EntityManager em = emf.createEntityManager();
		Employee employee = em.find(Employee.class, 2L);
		System.out.println();

		// condition + fetch 설정 결과
		// 1. condition=false, fetch=EAGER	-> inner join  , inner join
		// 2. condition=false, fetch=LAZY 	-> select , select * 2
		// 3. condition=true, fetch=EAGER	-> left outer join , left outer join
		// 4. condition=true, fetch=LAZY 	-> select , select * 2
		// 즉, fetch=LAZY 이면 condition 의 설정이 무시됨.
		// System.out.println(employee.getName() + " 직원이 검색됨");
		System.out.println(employee.getName() + "의 부서 : " + employee.getDept().getName());
	}
	
	/** 엔티티 수정
	 * 
	 * 사원의 부서 이동
	 */
	private static void dataUpdate(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 신규 부서 등록
		Department department = new Department();
		department.setName("영업부");
		em.persist(department);
		
		// 부서 변경
		Employee employee = em.find(Employee.class, 1L);
		employee.setDept(department);
		em.getTransaction().commit();
	}
	
	/** 다대일 관계에서 엔티티 삭제 
	 * 
	 * 생각해보기 >>
	 * 1. 부서 삭제시, 삭제되는 부서를 참조하는 사원 데이터가 존재
	 * 2. 부서를 참조하고 있는 데이터에 대해 참조관계를 끊으면 됨
	 * 	  사원 정보의 부서 정보는 없는 상태가 됨(null)
	 * 
	 * 	  Referential integrity constraint violation
	 * 	  참조 무결성 위배 : 1L 부서에 소속된 사원이 있음
	 * 
	 * 3. 이때, Employee 클래스의 dept 멤버에 설정된 optional 속성 정보가 삭제와 문제 없는지 고려해야 함
	 * 
	 *    @ManyToOne(optional = false)
	 *    참조변수의 값에 null 을 허용하지 않겠다는 의미
	 *    -> INNER JOIN 이 가능
	 * 	  -> setDept(null); 로 설정하면 설정정보와 맞지 않게 되어 Exception 발생
	 * 	  -> optional=false 구문 삭제
	 * 	  
	 * 4. JPA 를 떠나, 테이블 관계상에서 삭제시 CASCADE 설정을 이용하여 삭제되도록 하려면 어떻게 해야하는가?
	 * 	  -> 양방향 연관관계 설정이 필요..
	*/
	private static void dataDelete(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 부서에 대한 참조관계 제거
		Employee employee1 = em.find(Employee.class, 1L);
		employee1.setDept(null);
		Employee employee2 = em.find(Employee.class, 2L);
		employee2.setDept(null);
		
		Department department = em.find(Department.class, 1L);
		em.remove(department);
		em.getTransaction().commit();
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
