package com.example.demo.service;

import com.example.demo.exception.MissingFieldException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entity.Employee;
import com.example.demo.model.EmployeeModel;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) throws ResourceNotFoundException {
        return employeeRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Employee updateEmployee(Long id, EmployeeModel model) throws ResourceNotFoundException {
        Employee result = employeeRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        Optional.ofNullable(model.getName()).ifPresent(result::setName);
        return employeeRepository.save(result);
    }

    @Override
    public void deleteEmployee(Long id) throws ResourceNotFoundException {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee createEmployee(EmployeeModel model) throws MissingFieldException {

        Employee employee = new Employee(Optional.ofNullable(model.getName()).orElseThrow(MissingFieldException::new));
        return employeeRepository.save(employee);
    }
}
