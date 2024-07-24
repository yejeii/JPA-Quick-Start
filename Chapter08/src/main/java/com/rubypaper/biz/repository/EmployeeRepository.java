package com.rubypaper.biz.repository;

import com.rubypaper.biz.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByName(String name);

    // 페이징 처리
    // WHERE e.name like '%'||?1||'%';
    Page<Employee> findByNameContaining(String name, Pageable paging);

    // WHERE e.name like '%'||?1||'%'
    // OR e.mailId like '%'||?2||'%'
    List<Employee> findByNameContainingOrMailIdContaining(String name, String mailId);

    // WHERE e.mailId like '%'||?2||'%'
    // ORDER BY e.name DESC;
    List<Employee> findByMailIdContainingOrderByNameDesc(String mailId);

//    @Query("SELECT emp FROM Employee emp WHERE emp.name like %?1%")
    @Query("SELECT emp "
        + "FROM Employee emp "
        + "WHERE emp.name like %:name% "
        + "OR emp.mailId like %:mailId%")
    List<Employee> findByJPQL(@Param("name") String name,
                              @Param("mailId") String email);

    // 네이티브 쿼리 추가
    // nativeQuery = true : 영속 컨테이너가 해당 SQL 을 일반적인 SQL 로 인지하도록 함
    @Query(value = "select id, name, salary "
        + "from s_emp "
        + "where name like '%'||?1||'%' "
        + "order by id desc", nativeQuery = true)
    List<Object[]> findByNativeQuery(String name);
}
