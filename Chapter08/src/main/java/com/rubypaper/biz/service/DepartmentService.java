package com.rubypaper.biz.service;

import com.rubypaper.biz.domain.Department;
import com.rubypaper.biz.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("deptService")
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository repository;

    @Transactional
    public void insertDepartment(Department department) {
        //repository.insertDepartment(department);
        repository.save(department);
    }

    public Department getDepartment(Department department) {
        //return repository.getDepartment(department);
        return repository.findById(department.getDeptID()).orElse(null);
    }
}
