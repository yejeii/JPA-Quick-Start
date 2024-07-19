package com.rubypaper.biz.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.DepartmentWithCascade;
import com.rubypaper.biz.domain.EmployeeWithCascade;

/** 양방향 통신에서 영속성 전이 테스트 */
public class ManyToOneBothWayWithCascadeClient {

    @Test
	public void run() {

		// 엔티티 매니저 팩토리 생성
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter04");
		
		try {
			// dataInsertWithPersistenceCascade(emf);
            // dataDeleteWithPersistenceCascade(emf);
            dataDeleteWithOrphanRemoval(emf);
            // dataDeleteWithNull(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

    /** 영속성 전이를 통한 부모-자식 등록
     * 
     * cascade=PERSIST
     * Employee 엔티티(자식 테이블)가 관리 상태로 전환될 때 연관관계에 있는 Department 엔티티도 동일한 관리 상태로 설정
     */
    private static void dataInsertWithPersistenceCascade(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		// 부서 등록
		DepartmentWithCascade department = new DepartmentWithCascade();
		department.setName("보안부");
		em.persist(department);

		// org.hibernate.TransientPropertyValueException: object references an unsaved transient instance 에러
		// 두 엔티티의 상태가 다른데, 양방향 관계설정으로 사원 엔티티에서 부서 정보를 설정하려고 했기 때문에 생긴 에러

		// 따라서, 정상적으로 사원이 등록되려면, 부서 정보가 영속 상태임을 확인한 후 사원 등록이 이루어져야 함.
		// 그래서.. 매번 확인할 것이냐?

		// -> 영속성 전이 활용

		// 직원 등록 (Employee ---> Department 참조)
		EmployeeWithCascade employee1 = new EmployeeWithCascade();
		employee1.setName("보보");
		employee1.setDept(department);
		em.persist(employee1);

		em.getTransaction().commit();
		em.close();
	}

    private static void dataSelect(EntityManagerFactory emf) {
		
		EntityManager em = emf.createEntityManager();
		DepartmentWithCascade department = em.find(DepartmentWithCascade.class, 1L);
		
		System.out.println("검색된 부서 : " + department.getName());
		System.out.println("부서에 소속된 직원 명단");
		for(EmployeeWithCascade employee : department.getEmployeeList()) {
			System.out.println(employee.getName() + "(" + employee.getDept().getName() + ")");
		}
		em.close();
	}

    /** 영속성 전이를 활용한 엔티티 삭제 
     * 
     * 부서가 삭제될 때, 해당 부서에 속한 사원 정보도 함께 삭제 처리
     */
    private static void dataDeleteWithPersistenceCascade(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 부서 검색
		DepartmentWithCascade department = em.find(DepartmentWithCascade.class, 3L);
		
		// 부서에 등록된 직원 삭제
        // CASECADE 전에는 일일이 삭제시켜야 하는 번거로움..
        // -> CASCADE 로 자동 처리되게 하자
		// List<EmployeeWithCascade> employeeList = department.getEmployeeList();
		// for(EmployeeWithCascade employee : employeeList) {
			// em.remove(employee);
		// }

        // 부서 삭제
		em.remove(department);
		
		em.getTransaction().commit();
		em.close();
				
	}

    /** 고아 객체 삭제 
     * 
     * 고아 : 부모를 잃고 홀로된 객체
     * 부모 엔티티를 삭제할 대, 연관된 자식 엔티티가 부모를 잃은 고아가 됨.
     * 
     * 고아 객체 제거는 부모와의 연관관계에서 제외된 객체를 자동으로 삭제하는 기능
     */
    private static void dataDeleteWithOrphanRemoval(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 삭제할 부서 검색
		DepartmentWithCascade department = em.find(DepartmentWithCascade.class, 1L);
		List<EmployeeWithCascade> employeeList = department.getEmployeeList();
        System.out.println("!!");
        System.out.println(department.getName()+ ",  "+department.getEmployeeList());
        if(department.getEmployeeList() == employeeList) {
            System.out.println("true");
        }
		
		// 부서에 속한 모든 사원을 조회

        // 영속성 컨테이너에서 부서와 사원의 연관관계 제거
        // 제거한 위치 : 영속 컨테이너
        // 차이점 발생 : 테이블에서 발생함!
        //              테이블에는 해당 부서에 속한 사원 정보가 아직 존재하는 차이 발생!
        //              부서 엔티티와 테이블 정보가 다름 -> 사원 정보는 DELETE 대상이로구나!
        //              -> 테이블에서 부서 정보는 존재하나, 연관된 사원 정보는 DELETE 됨
        //              -> 사원은 없는 부서만 남게 됨 
        // 
        // 테이블에서 해당 부서에 속한 사원 정보를 영속성 컨테이너가 자동으로 삭제
        // 부서 엔티티에 사원 멤버에 orphanRemoval = true 로 설정되어 있기 때문
		employeeList.clear();	
		
		em.getTransaction().commit();
		em.close();	
	}

    /** 고아 객체 null 로 처리 
     * 
     * 부서가 고아 객체가 되더라도 자동으로 삭제되지 않도록 처리
     * -> 사원 정보에 대한 UPDATE 문이 발생할 것
     */
    private static void dataDeleteWithNull(EntityManagerFactory emf) {
		
		// 엔티티 매니저 생성, 트랜잭션 시작
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		// 삭제할 부서 검색
		DepartmentWithCascade department = em.find(DepartmentWithCascade.class, 6L);
		
		// 사원의 부서 정보 수정
		List<EmployeeWithCascade> employeeList = department.getEmployeeList();

        for(EmployeeWithCascade employee : employeeList) {
            
            // 방법1. setDept(null) 로 설정
            employee.setDept(null);

            // 방법2. 메서드 추가하여 null 처리
            // employee.standby();
        }
        
        // 부서 삭제 (원할 경우)
        // em.remove(department);

		em.getTransaction().commit();
		em.close();
				
	}

}
