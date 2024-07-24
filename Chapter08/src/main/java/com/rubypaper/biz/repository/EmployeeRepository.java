package com.rubypaper.biz.repository;

import com.rubypaper.biz.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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

    @Query("SELECT emp FROM Employee emp WHERE emp.name like %?1%")
    List<Employee> findByJPQL(String name);

}
