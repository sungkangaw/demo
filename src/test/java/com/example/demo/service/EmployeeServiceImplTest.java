package com.example.demo.service;

import com.example.demo.exception.MissingFieldException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entity.Employee;
import com.example.demo.model.EmployeeModel;
import com.example.demo.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeServiceImpl testInstance;

    private final Employee employee = new Employee(1L,"employee1");
    private final Employee employee2 = new Employee(1L,"employee2");

    @Before
    public void before() {
        testInstance = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    public void getAllEmployees_success() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));

        List<Employee> result = testInstance.getAllEmployees();
        verify(employeeRepository, times(1)).findAll();
        assertEquals(result.size(), 1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getEmployeeById_notFound() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        testInstance.getEmployeeById(2L);
    }

    @Test
    public void getEmployeeById_found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = testInstance.getEmployeeById(1L);
        assertEquals((long)result.getId(), 1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateEmployee_notFound() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        testInstance.updateEmployee(2L, EmployeeModel.builder().name("employee2").build());
    }

    @Test
    public void updateEmployee_success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee2)).thenReturn(employee2);

        Employee result = testInstance.updateEmployee(1L, EmployeeModel.builder().name("employee2").build());
        assertEquals((long)result.getId(), 1L);
        assertEquals(result.getName(),"employee2");
    }

    @Test
    public void deleteEmployee_verifyCall() {
        doNothing().when(employeeRepository).deleteById(1L);

        testInstance.deleteEmployee(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test(expected = MissingFieldException.class)
    public void createEmployee_missingField() {
        testInstance.createEmployee(EmployeeModel.builder().build());
    }

    @Test
    public void createEmployee_success() {
        Employee employeeEntity = new Employee("employee1");
        when(employeeRepository.save(employeeEntity)).thenReturn(employee);

        Employee result = testInstance.createEmployee(EmployeeModel.builder().name("employee1").build());
        assertEquals((long)result.getId(), 1L);
        assertEquals(result.getName(),"employee1");
    }
}
