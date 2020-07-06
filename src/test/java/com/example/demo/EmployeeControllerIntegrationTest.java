package com.example.demo;

import com.example.demo.controller.EmployeeController;
import com.example.demo.entity.Employee;
import com.example.demo.model.EmployeeModel;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	@MockBean
	private UserServiceImpl userDetailsService;

	@MockBean
	private EmployeeService employeeService;

	private MockMvc mvc;

	private final String USERNAME = "user";
	private final String PASSWORD = "$2a$04$I9Q2sDc4QGGg5WNTLmsz0.fvGv3OjoZyj81PrSFyGOqMphqfS2qKu";
	private final String EMPLOYEE_JSON_STRING = "{\"name\":\"employee1\"}";

	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();

		when(userDetailsService.loadUserByUsername(USERNAME))
				.thenReturn( new org.springframework.security.core.userdetails.User(USERNAME, PASSWORD, Arrays.asList( new SimpleGrantedAuthority("ROLE_ADMIN"))));

		EmployeeModel model = EmployeeModel.builder().name("employee1").build();
		when(employeeService.createEmployee(model)).thenReturn(new Employee(1L, "employee1"));
		when(employeeService.updateEmployee(1L, model)).thenReturn(new Employee(1L, "employee1"));
	}

	@WithMockUser(USERNAME)
	@Test
	public void getEmployee_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void getEmployee_shouldFail() throws Exception {
		mvc.perform(get("/employees").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(401));
	}

	@WithMockUser(USERNAME)
	@Test
	public void postEmployee_shouldSucceedWith200() throws Exception {

		mvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(EMPLOYEE_JSON_STRING))
				.andExpect(status().isCreated());
	}

	@Test
	public void postEmployee_shouldFail() throws Exception {
		mvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(EMPLOYEE_JSON_STRING))
				.andExpect(status().is(401));
	}

	@WithMockUser(USERNAME)
	@Test
	public void deleteEmployee_shouldSucceedWith200() throws Exception {
		mvc.perform(delete("/employees/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteEmployee_shouldFail() throws Exception {
		mvc.perform(delete("/employees/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(401));
	}

	@WithMockUser(USERNAME)
	@Test
	public void patchEmployee_shouldSucceedWith200() throws Exception {
		mvc.perform(patch("/employees/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(EMPLOYEE_JSON_STRING))
				.andExpect(status().isOk());
	}

	@Test
	public void patchEmployee_shouldFail() throws Exception {
		mvc.perform(patch("/employees/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(EMPLOYEE_JSON_STRING))
				.andExpect(status().is(401));
	}
}

