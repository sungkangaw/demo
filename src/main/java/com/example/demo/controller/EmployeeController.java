package com.example.demo.controller;


import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entity.Employee;
import com.example.demo.model.EmployeeModel;
import com.example.demo.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

	private EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/employees")
	public List<EmployeeModel> retrieveAllEmployees() {
		return employeeService.getAllEmployees().stream()
				.map(x -> EmployeeModel.builder().name(x.getName()).build()).collect(Collectors.toList());
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<EmployeeModel> retrieveEmployee(@PathVariable long id) throws ResourceNotFoundException {
		Employee employee = employeeService.getEmployeeById(id);
		return new ResponseEntity<>(
				EmployeeModel.builder()
						.name(employee.getName())
						.build(),
				HttpStatus.OK);
	}

	@DeleteMapping("/employees/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employeeService.deleteEmployee(id);
	}

	@PostMapping("/employees")
	public ResponseEntity<EmployeeModel> createEmployee(@RequestBody EmployeeModel employeeModel) {
		Employee savedEmployee = employeeService.createEmployee(employeeModel);
		return new ResponseEntity<>(
				EmployeeModel.builder()
						.name(savedEmployee.getName())
						.build(),
				HttpStatus.CREATED);

	}

	@PatchMapping("/employees/{id}")
	public ResponseEntity<EmployeeModel> updateEmployee(@PathVariable long id, @RequestBody EmployeeModel employeeModel) throws ResourceNotFoundException{
		Employee updatedEmployee = employeeService.updateEmployee(id, employeeModel);
		return new ResponseEntity(
				EmployeeModel.builder()
						.name(updatedEmployee.getName())
						.build(),
				HttpStatus.OK);
	}
}