package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee2;

public class Employee2ServiceClient {

    /** 묵시적 flush, 명시적 flush 실습
     * 
     * >> 핵심 << 
     * flush() 가 곧 DB 까지의 INSERT 를 의미하진 않음!
     * 트랜잭션이 커밋되어야만 INSERT 문장이 flush 되어 영구 반영됨!!
     */
    @Test
    public void shouldFlushOnlyCommit() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();

        // 커밋할 때만 flush 동작
        em.setFlushMode(FlushModeType.COMMIT);

        // 트랜잭션 생성
        EntityTransaction tx = em.getTransaction();

		try {
            // 엔티티 생성
            Employee2 employee = new Employee2();
            employee.setName("길동이");

            // 트랜잭션
            tx.begin();
            em.persist(employee);
            em.flush();     // 명시적 flush 동작 : INSERT 문장 발생, but DB 와 동기화 X
            
            // tx.commit(); // 묵시적 flush 동작 후 동기화
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
	}

    /** 1차 캐시의 동일성 비교
     * 
     * >> 핵심 <<
     * 엔티티를 검색하지만, SELECT 문장 작성 X
     */
    @Test
    public void shouldEntityEquals() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 엔티티 생성
            Employee2 employee = new Employee2();
            employee.setName("길동이");

            // 트랜잭션
            tx.begin();
            em.persist(employee);
            tx.commit(); 

            // 사원 검색
            Employee2 findEmp1 = em.find(Employee2.class, 1L);
            Employee2 findEmp2 = em.find(Employee2.class, 1L);

            // 엔티티에 대한 동일성 비교 
            // ( 1차 캐시의 장점, 1차 캐시의 구조가 key, value 형태로 관리되므로 )
            // find() 호출했지만, SELECT 문장 발생 X
            // 즉, 1차 캐시에서 바로 찾아낸 것. -> 애플리케이션 성능에 도움
            if(findEmp1 == findEmp2) {
                System.out.println("동일한 객체를 참조중!");
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    /** SELECT 문장이 발생하는 경우
     * 
     * 1. 기존 테이블이 유지가 되도록 해야함
     *    persistence.xml 에서 ddl - "update"
     * 
     * 2. 기존 데이터 검색
     *      - 영속성 컨텍스트가 비어있는 상태
     *      - 엔티티 검색
     *      - 1차 캐시에 해당되는 엔티티가 없는 상태
     *      - DB 가자~ -> SELECT 문장 작성
     */
    @Test
    public void shouldSelect() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();

        try {
            // 사원 검색
            Employee2 findEmp1 = em.find(Employee2.class, 1L);
            Employee2 findEmp2 = em.find(Employee2.class, 1L);

            if(findEmp1 == findEmp2) {
                System.out.println("동일한 객체를 참조중!");
            }
        } catch (Exception e) {
        } finally {
            emf.close();
        }
    }

    /** 엔티티 수정
     * 
     *  방법1. 더티 체크가 발생하도록 코드 작성 : select -> update
     *  방법2. merge() 호출
     */
    @Test
    public void shouldDirtyCheck() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 사원 검색
            Employee2 findEmp1 = em.find(Employee2.class, 1L);

            tx.begin();
            findEmp1.setName("이름 수정");
            tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    /** 분리 상태에서의 엔티티 수정
     * 
     * ddl - "create" 로 수정
     * 
     * 엔티티 생성 및 등록
     * 분리 : em.clear()
     * 엔티티 수정 
     * 
     * 결과 >> DB 에 반영 X
     */
    @Test
    public void shouldNotUpdateAfterClear() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 엔티티 생성
            Employee2 employee = new Employee2();
            employee.setName("길동이");

            // 트랜잭션
            tx.begin();
            em.persist(employee);

            // 묵시적 flush 실행 -> insert 실행
            // persistence.xml 에 의해 테이블에 생성되는 옵션
            // DB 테이블에도 없는 데이터
            // 영속성 컨텍스트에 없는 엔티티
            tx.commit();    

            // 엔티티 분리
            em.clear();

            // 준영속 엔티티 수정
            tx.begin();
            employee.setName("이름 수정");
            tx.commit();

            // 영속성 컨텍스트와 분리된 상태 -> DB 와 무관한 객체가 됨
            // -> 트랜잭션에 넣어도 DB 에 UPDATE 문 전달 X

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    /** 분리 상태에서 엔티티 수정 후 DB 반영 */
    @Test
    public void shouldUpdateAfterClear() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 엔티티 생성
            Employee2 employee = new Employee2();
            employee.setName("길동이");

            // 트랜잭션
            tx.begin();
            em.persist(employee);

            // 묵시적 flush 실행 -> insert 실행
            // persistence.xml 에 의해 테이블에 생성되는 옵션
            // DB 테이블에도 없는 데이터
            // 영속성 컨텍스트에 없는 엔티티
            tx.commit();    

            // 엔티티 분리
            em.clear();

            // 준영속 엔티티 수정
            // tx.begin();
            // employee.setName("이름 수정");
            // tx.commit();

            // 영속성 컨텍스트와 분리된 상태 -> DB 와 무관한 객체가 됨
            // -> 트랜잭션에 넣어도 DB 에 UPDATE 문 전달 X

            // merge() 이용해서 수정
            tx.begin();
            employee.setName("이름 수정");
            // 매개변수는 준영속 상태의 엔티티
            // 반환 결과 : 영속 상태의 엔티티
            Employee2 mergedEmp = em.merge(employee);  
            /*
             * 1. 준영속 상태의 엔티티의 식별자를 이용, 1차 캐시에서 조회 후 없으면 식별자를 통해 DB 에서 조회 -> SELECT 전송
             * 
             *    1차 캐시에 엔티티 등록
             * 
             * 2. 준영속 상태의 엔티티를 1차 캐시에 반영
             *    스냅샷과 차이 발생
             * 
             * 3. commit() -> 묵시적 flush 발생
             *    스냅샷과의 차이를 DB 에 반영하기 위해 UPDATE SQL 문 작성, 전송
             */
            
            tx.commit();

            System.out.println("employee 의 상태 : " + em.contains(employee));
            System.out.println("mergedEmp 의 상태 : " + em.contains(mergedEmp));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    /** merge() 이용하여 persist() 처럼 동작되도록
     * 
     * persist() -> insert 발생
     * 
     * merge() 가 어떻게 생성 상태(insert)인지 분리 상태(update)인지를 어떻게 구분함?
     */
    @Test
    public void shouldMergeActPersist() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 엔티티 생성
            Employee2 employee = new Employee2();
            employee.setName("길동이");

            // 트랜잭션
            tx.begin();
            // employee 엔티티는 생성 상태
            em.merge(employee);

            // 묵시적 flush 실행 -> insert 실행
            // DB 테이블에도 없는 데이터
            // 영속성 컨텍스트에 없는 엔티티
            tx.commit();    

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    /** merge() + 식별자 임의 설정
     * 
     * 결과 >>
     * SELECt -> INSERT
     * 
     * 의미 >>
     *  1. 1차 캐시에서 검색 ( 식별자가 있기 때문 )
     *  2. DB 에 가서 식별자 값으로 검색 -> SELECT 작성 및 전송
     *  3. DB 에서 not found -> INSERT 작성 및 전송
     */
    @Test
    public void shouldMergeSetIdPersist() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 엔티티 생성 - 식별자 설정
            Employee2 employee = new Employee2();
            employee.setId(1L);
            employee.setName("길동이");

            // 트랜잭션
            tx.begin();
            // employee 엔티티는 생성 상태
            em.merge(employee);

            // 묵시적 flush 실행 -> insert 실행
            // DB 테이블에도 없는 데이터
            // 영속성 컨텍스트에 없는 엔티티
            tx.commit();    

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    /** merge() 어떻게 생성 상태(INSERT)와 분리 상태(UPDATE)를 구분하는가?
     *  
     *  1. SELECT 또는 INSERT 발생하는 경우
     *     merge() 에 전달되는 매개변수 엔티티의 식별자 값 유무
     * 
     *      - 식별자가 없는 경우 -> INSERT 발생
     *      - 식별자가 있는 경우 -> SELECT 발생 후 INSERT 발생
     *          
     *  2. UPDATE 가 발생하는 경우
     *     merge() 에 전달되는 매개변수 엔티티가 DB 에 존재하고 엔티티가 수정이 된 경우
     * 
     * 실습 >>
     * ddl - "update"
     * 
     * 결과 >>
     * SELECt -> UPDATE
     * 
     * 의미 >>
     *  1. 1차 캐시에서 검색 ( 식별자가 있기 때문 )
     *  2. DB 에 가서 식별자 값으로 검색 -> SELECT 작성 및 전송
     *  3. DB 에 있는 엔티티임을 확인 
     *  4. DB 에 수정 -> UPDATE 작성 및 전송
     */
    @Test
    public void shouldMergeFlushPersist() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.COMMIT);  // 커밋시에만 flush 동작
        EntityTransaction tx = em.getTransaction();

        try {
            // 엔티티 생성 - 식별자 설정
            // 기존에 있는 데이터 기준으로 식별자 지정
            Employee2 employee = new Employee2();
            employee.setId(1L);
            employee.setName("이름 수정");

            // 트랜잭션
            tx.begin();
            // employee 엔티티는 생성 상태
            em.merge(employee);
            tx.commit();    

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    /** refresh() 를 통한 엔티티 갱신
     * 
     * refresh()
     * 테이블의 변화를 엔티티에 반영
     * 테이블의 변화는 누군가가 나의 테이블 데이터를 수정한 경우,
     * application 에서 사용중인 엔티티를 테이블 기준으로 갱신
     * 
     * 실습 >> 
     * ddl - "create"
     */
    @Test
    public void shouldRefresh() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter03");
        EntityManager em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.COMMIT);  // 커밋시에만 flush 동작
        EntityTransaction tx = em.getTransaction();

        try {
            // 엔티티 생성 - 식별자 설정
            // 기존에 있는 데이터 기준으로 식별자 지정
            Employee2 employee = new Employee2();
            employee.setName("길동이");

            // 트랜잭션
            tx.begin();
            em.persist(employee);
            tx.commit();    

            // 일정 시간 대기
            // 직접 테이블에서 데이터 수정 ( 제 3자가 데이터 수정하는 것을 가정 )
            for (int i = 0; i < 30; i++) {
                Thread.sleep(1000); // 1초
                System.out.println("수정 중 ZZZ..");
            }

            // 현재 사용중인 엔티티를 DB 기준으로 갱신
            em.refresh(employee);
            System.out.println("갱신된 사원 정보 : " + employee.getName());

		} catch (Exception e) {
			e.printStackTrace();
            tx.rollback();
		} finally {
            em.close();
			emf.close();
		}
    }
}

