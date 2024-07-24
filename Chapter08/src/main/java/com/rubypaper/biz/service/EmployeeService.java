package com.rubypaper.biz.service;

import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("empService")
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public void insertEmployee(Employee employee) {
        //repository.insertEmployee(employee);
        repository.save(employee);
    }

    public void updateEmployee(Employee employee) {
        //repository.updateEmployee(employee);
        repository.save(employee);
    }

    public void deleteEmployee(Employee employee) {
        //repository.deleteEmployee(employee);
        repository.delete(employee);
    }

    public Employee getEmployee(Employee employee) {
        //return repository.getEmployee(employee);
        return repository.findById(employee.getId()).orElse(null);
    }

    public List<Object[]> getEmployeeList(Employee employee) {
        
        /* 페이징 처리
         * of() - 첫 번째 인자 : 페이지 번호 (0부터 시작)
         *        두 번째 인자 : 검색할 데이터 수
         */
        // 첫 번째 페이지에 해당하는 3개의 사원 정보 조회
        //Pageable paging = PageRequest.of(pageNumber - 1, 3);

        // 페이징 처리에 정렬 방식 추가
        // Sort 클래스 활용
        // id 변수 값을 기준으로 DESC 정렬
        //Pageable paging = PageRequest.of(pageNumber -1,3, Sort.Direction.DESC, "id");

        // 여러 칼럼에 대한 다중 정렬
        // ORDER BY mailId DESC, salary ASC
        // LIMIT ? OFFSET ?
//        Pageable paging = PageRequest.of(pageNumber -1,3, Sort.by(new Sort.Order(Sort.Direction.DESC, "mailId"),
//                                                                            new Sort.Order(Sort.Direction.ASC, "salary")));

        //return repository.getEmployeeList(employee);
        //return (List<Employee>) repository.findAll();
        //return (List<Employee>) repository.findByName(employee.getName());
//        return repository.findByNameContaining(employee.getName(), paging);
        //return (List<Employee>) repository.findByNameContainingOrMailIdContaining(employee.getName(), employee.getMailId());
        //return (List<Employee>) repository.findByMailIdContainingOrderByNameDesc(employee.getMailId());
//        return (List<Employee>) repository.findByJPQL(employee.getName(), employee.getMailId());
        return repository.findByNativeQuery(employee.getName());
    }

}
