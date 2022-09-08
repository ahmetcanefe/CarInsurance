package com.CarPolicy.CarPolicy.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CarPolicy.CarPolicy.entities.Car;
import com.CarPolicy.CarPolicy.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

	Customer findByEmail(String email);
	
	Customer findByNationalIdentity(String nationalIdentity);

	Page<Customer> findByRoles_Name(String name, Pageable pageable);
	
	Page<Customer> findByNameContainingIgnoreCase(String query,Pageable pageable);
	
	Page<Customer> findByRoles_NameAndNameContainingIgnoreCase(String name,String query, Pageable pageable);
}
