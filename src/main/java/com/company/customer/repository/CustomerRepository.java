package com.company.customer.repository;

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
}
