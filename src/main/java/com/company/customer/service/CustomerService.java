package com.company.customer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.customer.entity.CustomerEntity;
import com.company.customer.exception.CustomerNotFoundException;
import com.company.customer.model.Customer;
import com.company.customer.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public Customer addCustomer(Customer customer) {
		customerRepository.save(CustomerEntity.builder().firstName(customer.getFirstName())
				.lastName(customer.getLastName()).dateOfBirth(customer.getDateOfBirth()).build());
		return customer;
	}

	public List<Customer> getCustomers() {
		return customerRepository.findAll().stream().map(c -> Customer.builder().firstName(c.getFirstName())
				.lastName(c.getLastName()).dateOfBirth(c.getDateOfBirth()).build()).collect(Collectors.toList());
	}

	public List<Customer> getCustomersByLastName(String lastName) throws CustomerNotFoundException {
		Optional<List<CustomerEntity>> entityList = customerRepository.getCustomerByLastName(lastName);
		if (entityList.isEmpty() || entityList.get().isEmpty())
			throw new CustomerNotFoundException(lastName);
		
		return entityList.get().stream().map(c -> Customer.builder().firstName(c.getFirstName())
				.lastName(c.getLastName()).dateOfBirth(c.getDateOfBirth()).build()).collect(Collectors.toList());
	}
}
