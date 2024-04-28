package com.company.customer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

//import static org.mockito.Mockito.when;

//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.TestInstance.Lifecycle;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import com.example.customer.controller.CustomerController;
//import com.example.customer.model.Customer;
//import com.example.customer.service.CustomerService;
//import com.example.integration.test.GenerateJwtToken;
//
//
//@TestInstance(Lifecycle.PER_CLASS)
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = CustomerControllerUnitTest.class)
//@WebMvcTest(value = CustomerController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@EnableWebMvc
//public class CustomerControllerUnitTest implements WebMvcConfigurer {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@MockBean
//	private CustomerService customerService;
//	
//	 @Override
//	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
//	        // Add more resource handlers as needed for other static resources
//	    }
//
//	public List<Customer> mockCustomers() {
//		List<Customer> customerList = new ArrayList<>();
//		customerList.add(Customer.builder().firstName("Arun").lastName("Garimella")
//				.dateOfBirth(LocalDate.of(1992, 06, 14)).build());
//		return customerList;
//	}
//
//	@Test
//	public void fetchCustomers() throws Exception {
//		String validToken = GenerateJwtToken.generateToken();
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Authorization", "Bearer " + validToken);
//		when(customerService.getCustomers()).thenReturn(mockCustomers());
//
//		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/api/customers/").headers(headers)
//				.accept(MediaType.APPLICATION_JSON);
//
//		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//		System.out.println(result.getResponse().getContentAsString());
//	}
//}

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.company.customer.model.Customer;
import com.company.customer.service.CustomerService;
import com.company.customer.validation.JsonSchemaValidator;

@ContextConfiguration(classes = { CustomerControllerUnitTest.class })
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CustomerControllerUnitTest {

	@Mock
	private CustomerService customerService;

	@Mock
	private JsonSchemaValidator jsonSchemaValidator;

	private MockMvc mockMvc;

	@InjectMocks
	private CustomerController customerController;

	@BeforeEach
	public void setUp() {

		mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
	}

	private List<Customer> getMockCustomers() {

		List<Customer> customerList = new ArrayList<>();
		customerList.add(Customer.builder().firstName("Arun").lastName("Garimella")
				.dateOfBirth(LocalDate.of(1992, 06, 14)).build());
		return customerList;
	}

	@Test
	public void testGetCustomers() throws Exception {

		HttpHeaders headers = new HttpHeaders();

		when(customerService.getCustomers()).thenReturn(getMockCustomers());
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/customers/").contentType(MediaType.APPLICATION_JSON)
				.headers(headers)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].firstName").value("Arun"))
				.andExpect(jsonPath("$[0].lastName").value("Garimella"));

	}

	@Test
	public void testGetCustomerByLastName() throws Exception {
		when(customerService.getCustomersByLastName(any())).thenReturn(getMockCustomers());
		mockMvc.perform(get("/v1/api/customers/search").param("lastName", "Garimella"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$[0].firstName").value("Arun"))
				.andExpect(jsonPath("$[0].lastName").value("Garimella"));

	}

	@Test
	public void testAddCustomer() throws Exception {
		doNothing().when(jsonSchemaValidator).validateSchema(any());

		when(customerService.addCustomer(any())).thenReturn(Customer.builder().firstName("Arun").lastName("Garimella")
				.dateOfBirth(LocalDate.of(1992, 06, 14)).build());

		mockMvc.perform(post("/v1/api/customers/").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "\"firstName\":\"Arun\",\r\n" + "\"lastName\":\"Garimella\",\r\n"
						+ "\"dateOfBirth\":\"1992-06-14\"\r\n" + "}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("Arun"));
	}
}
