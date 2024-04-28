package com.company.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.customer.api.CustomerAPI;
import com.company.customer.exception.CustomerNotFoundException;
import com.company.customer.exception.CustomerValidationException;
import com.company.customer.model.Customer;
import com.company.customer.service.CustomerService;
import com.company.customer.validation.JsonSchemaValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/v1/api/customers")
public class CustomerController implements CustomerAPI {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private JsonSchemaValidator jsonSchemaValidator;

	@Override
	@PostMapping("/")
	@CrossOrigin(origins = "http://localhost:8090")
	public Customer addCustomer(@RequestBody Customer customer)
			throws JsonMappingException, JsonProcessingException, CustomerValidationException {
		jsonSchemaValidator.validateSchema(customer);
		return customerService.addCustomer(customer);
	}

	@Override
	@GetMapping("/")
	public List<Customer> getCustomers() {
		return customerService.getCustomers();
	}

	@Override
	@GetMapping("/search")
	public List<Customer> getCustomersByLastName(@RequestParam("lastName") String lastName)
			throws CustomerNotFoundException {
		return customerService.getCustomersByLastName(lastName);
	}

}
