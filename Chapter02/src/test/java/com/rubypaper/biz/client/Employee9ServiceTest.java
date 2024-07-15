package com.rubypaper.biz.client;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Test;

import com.rubypaper.biz.domain.Employee8;
import com.rubypaper.biz.domain.Employee9;

/**
 * 본 테스트 케이스를 실행하면 오류 발생
 * 
 * 현재 사용중인 h2 DB 최신 버전에 버그 존재..
 *  - 해결1. h2 1.4.200 버전을 사용하면 오류 발생 X
 *  - 해결2. persistence.xml JDBC URL 정보 수정
 *           2.1 변경전
 *               jdbc:h2:tcp://localhost/~/test
 *           2.2 변경후
 *               jdbc:h2:tcp://localhost/~/test;MODE=MySQL1
 *  - 해결3. @GeneratedValue의 Strategy 변경
 *           3.1 변경 전
 *               @GeneratedValue(strategy=GenerationType.IDENTITY)
 *           3.2 변경 후
 *               @GeneratedValue(strategy=GenerationType.SEQUENCE)
 */
public class Employee9ServiceTest {

    @Test
    public void run() {
        // <persistence-unit name="Chapter02" /> 의 설정 정보 참조
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Chapter02");
		
        try {
            allocationSizePropTest(emf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			emf.close();
		}
    }

    /**
     * @GeneratedValue(strategy=GenerationType.SEQUENCE, allocationSize=1)
     * 
     * Hibernate: drop table Employee9 if exists
     * 
     * Hibernate: drop sequence if exists Employee9_sequence
     * 
     * Hibernate: create sequence Employee9_sequence start with 1 increment by 1
     * 
     * Hibernate: 
     *    create table Employee9 (
     *        id bigint not null,
     *        mailId varchar(255),
     *        name varchar(255),
     *        primary key (id)
     *    )
     *
     * Hibernate: 
     *     call next value for Employee9_GENERATOR
     * Hibernate: 
     *     insert 
     *     into
     *         Employee9
     *         (mailId, name, id) 
     *     values
     *         (?, ?, ?)
     */
    public void generatedValueSequenceAnoTest(EntityManagerFactory emf) throws InterruptedException {
		
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // 영속성 관리 엔터티 생성
            Employee8 employee = new Employee8();
            
            // employee.setId(1L);
            employee.setName("홍길동");
            employee.setMailId("hong");
            
            tx.begin();    
            
            System.out.println("등록 전 id - " + employee.getId()); // null

            // 영속성 관리를 위한 엔터티 등록
            em.persist(employee);
            
            System.out.println("등록 후 id - " + employee.getId());

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
	}

    /**
     * @SequenceGenerator(allocationSize=50) : 시퀀스를 한 번 조회할 떄마다 50씩 증가
     * 
     * Hibernate: drop table Employee9 if exists
     * 
     * Hibernate: drop sequence if exists Employee9_sequence
     * 
     * Hibernate: create sequence Employee9_sequence start with 1 increment by 50
     * 
     * Hibernate: 
     *     call next value for Employee9_sequence   
     * Hibernate: 
     *     call next value for Employee9_sequence   
     * Hibernate: 
     *     call next value for Employee9_sequence   
     * 
     * 시퀀스가 50씩 증가하는 것과 별개로 JPA 에서는 한 번에 증가된 50개의 값을 메모리에서 하나씩 꺼내어 사용
     * -> DB 와의 연결을 less
     * 
     * 
     */
    public void allocationSizePropTest(EntityManagerFactory emf) throws InterruptedException {
		
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();    

            // 영속성 관리 엔터티 생성
            for (int i = 0; i < 101; i++) {
                Employee9 employee = new Employee9();
                employee.setName("홍길동 " + i);
                employee.setMailId("hong " + i);
                
                // 영속성 관리를 위한 엔터티 등록
                em.persist(employee);
            }
            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
	}

}

