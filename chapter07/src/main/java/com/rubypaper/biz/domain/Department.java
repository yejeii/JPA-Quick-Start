package com.rubypaper.biz.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@ToString(exclude="employeeList")
@Data
@Entity
@Table(name="S_DEPT")
public class Department {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="DEPT_ID") 
    private Long deptID;

    private String name;

    // 연관관계 매핑
    @OneToMany(mappedBy="dept")
    private List<Employee> employeeList = new ArrayList<>();
    
}