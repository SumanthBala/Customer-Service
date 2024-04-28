package com.company.customer.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.company.customer.exception.CustomerNotFoundException;
import com.company.customer.exception.CustomerValidationException;
import com.company.customer.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Customer Service", description = "Customer Service API")
public interface CustomerAPI {

	@Operation(summary = "Fetch all Customers", description = "Fetches all customer entities and their data from data source")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
	List<Customer> getCustomers();

	@Operation(summary = "Add Customer", description = "Adds the customer to the customer data source")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "409", description = "Duplicate Customer") })
	Customer addCustomer(@RequestBody Customer customer)
			throws JsonMappingException, JsonProcessingException, CustomerValidationException;

	@Operation(summary = "Fetch Customers By LastName", description = "Fetches all the last name matching customer entities from datasource")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "404", description = "resource not found") })
	List<Customer> getCustomersByLastName(@RequestParam("lastName") String lastName) throws CustomerNotFoundException;
}
