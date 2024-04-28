package com.company.customer.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.company.customer.entity.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

	@Query("SELECT c FROM CustomerEntity c WHERE c.lastName = ?1")
	public Optional<List<CustomerEntity>> getCustomerByLastName(String lastName);
	
	@Query("SELECT c FROM CustomerEntity c WHERE c.firstName = ?1 and c.lastName = ?2 and c.dateOfBirth = ?3")
	public Optional<CustomerEntity> getCustomer(String firstName, String lastName,LocalDate dateOfBirth);
}
