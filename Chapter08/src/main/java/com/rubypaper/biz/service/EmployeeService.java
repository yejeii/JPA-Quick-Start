package com.rubypaper.biz.service;

import com.rubypaper.biz.domain.Employee;
import com.rubypaper.biz.persistence.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("empService")
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public void insertEmployee(Employee employee) {
        repository.insertEmployee(employee);
    }

    public void updateEmployee(Employee employee) {
        repository.updateEmployee(employee);
    }

    public void deleteEmployee(Employee employee) {
        repository.deleteEmployee(employee);
    }

    public Employee getEmployee(Employee employee) {
        return repository.getEmployee(employee);
    }

    public List<Employee> getEmployeeList(Employee employee) {
        return repository.getEmployeeList(employee);
    }
}
