package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee8;

/**
 * 본 테스트 케이스를 실행하면 오류 발생
 * 
 * 현재 사용중인 h2 DB 최신 버전에 버그 존재..
 *  - 해결1. h2 1.4.200 버전을 사용하면 오류 발생 X
 *  - 해결2. persistence.xml JDBC URL 정보 수정
 *           2.1 변경전
 *               jdbc:h2:tcp://localhost/~/test
 *           2.2 변경후
 *               jdbc:h2:tcp://localhost/~/test;MODE=MySQL
 *  - 해결3. @GeneratedValue의 Strategy 변경
 *           3.1 변경 전
 *               @GeneratedValue(strategy=GenerationType.IDENTITY)
 *           3.2 변경 후
 *               @GeneratedValue(strategy=GenerationType.SEQUENCE)
 */
public class Employee8ServiceTest {

    @Test
    public void run() {

        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		
        try {
			generatedValueIdentityAnoTest(emf);
			findEmployee(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    /**
     * 식별자에 @GeneratedValue 추가
     * -> 식별자 값을 hibernate 가 알아서 넣어준다.
     * -> hibernate 가 SYSTEM_SEQUENCE~~ 이름의 시퀀스 테이블이 생성하여 이를 참조하여 식별자를 할당함
     * -> insert SQL 문은 null 로 설정되나, DB 엔 시퀀스 테이블에 올라간 정보를 토대로 pk 값이 설정됨
     * 
     * >> persist() 액션 << 
     * Hibernate: 
     *  insert 
     *  into
     *      Employee8
     *      (id, mailId, name) 
     *  values
     *      (null, ?, ?)
     */
    public void generatedValueIdentityAnoTest(EntityManagerFactory emf) throws InterruptedException {
		
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 영속성 관리 엔터티 생성
            Employee8 employee = new Employee8();
            
            // employee.setId(1L);
            employee.setName("홍길동");
            employee.setMailId("hong");
            
            tx.begin();    
            
            System.out.println();
            System.out.println("영속 상태에 등록 전 id - " + employee.getId()); // null
            System.out.println();

            // 영속성 관리를 위한 엔터티 등록 : 캐시에 등록(K-V 쌍으로)
            // 이때의 Key 는 @ID 로 매핑한 식별자 값이 됨
            em.persist(employee);
            /* Hibernate: 
                insert 
                into
                    Employee8
                    (id, mailId, name) 
                values
                    (null, ?, ?) 
            */

            System.out.println();
            System.out.println("영속 상태 id - " + employee.getId());  // 1
            System.out.println();

            // id 값이 1이 되었다는 의미는 persist() 가 호출되면 id=null 로 설정된 INSERT 문을 생성하여 DB 에 전송되고,
            // DB 가 SYSTEM_SEQUENCE 시퀀스를 참조하여 id 칼럼에 증가된 식별자 값을 할당했다는 의미가 됨
            // 즉, DB 까지 한번 갔다와서 클라이언트 쪽에서 JPA 가 관리하는 Employee 객체에도 증가된 식별자 값이 할당되어 영속 컨텍스트에서 관리되고 있는 것
            // UPDATE 모드로 실행해보면 SYSTEM_SEQUENCE 는 2 가 되어 있음

            // 트랜잭션 커밋까지의 30초 동안 DB 에서 SELECT..
            // SELECT 결과 -> 데이터 존재 X
            // 트랜잭션이 커밋되지 않은 상태이므로 -> 동시성 제어를 위함 : 커밋도 안된 데이터가 DB 에 반영된다는 건 말이 안되므로
            for (int i = 0; i < 15; i++) {
                Thread.sleep(1000);
                System.out.println("ZZZZ");

                // if(i==13) {
                //     System.out.println("id 가 식별자값이랑 매핑되었나..? " + employee.getId());  // 1
                // }
            }

            // 트랜잭션 커밋 -> FLUSH 진행
            // -> 캐시에 저장된 엔티티 상태 변화를 DB 에 반영
            // -> "동기화" 진행
            tx.commit();
            // 1. 관리 상태의 엔티티에 대한 INSERT 구문 생성
            // 2. 자동으로 FLUSH 실행, 모든 DML 문 한 번에 DB 로 전송
            // 3. 실제로 트랜잭션 COMMIT, 영구적으로 저장

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
	}

    public void findEmployee(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();

        Employee8 findEmployee = em.find(Employee8.class, 1L);
        System.out.println("검색한 사원 정보");
        System.out.println(findEmployee.toString());
    }
}

