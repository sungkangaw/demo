package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entity.Employee;
import com.example.demo.model.EmployeeModel;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id) throws ResourceNotFoundException;

    Employee updateEmployee(Long id, EmployeeModel model) throws ResourceNotFoundException;

    void deleteEmployee(Long id);

    Employee createEmployee(EmployeeModel model);

}
