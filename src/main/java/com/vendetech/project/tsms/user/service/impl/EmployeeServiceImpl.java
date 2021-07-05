package com.vendetech.project.tsms.user.service.impl;

import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.user.mapper.EmployeeMapper;
import com.vendetech.project.tsms.user.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper  employeeMapper;
    @Override
    public List<Employee> selectByEmployeeName(String employeeName) {
        return employeeMapper.selectByEmployeeName(employeeName);
    }

    @Override
    public List<Employee> getOneByEmployeeNum(String employeeNum) {
        return employeeMapper.getOneByEmployeeNum(employeeNum);
    }
}
