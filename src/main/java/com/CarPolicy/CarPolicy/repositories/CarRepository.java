package com.CarPolicy.CarPolicy.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CarPolicy.CarPolicy.entities.Car;

public interface CarRepository extends JpaRepository<Car,Integer>{

	List<Car> findByCustomerIdAndIsActive(int userId,boolean isActive);
	
	Car findByLicensePlate(String licensePlate);
	
	List<Car> findByIsActive(boolean isActive); 
	
	Car findByLicensePlateAndCustomerId(String licensePlate, int customerId);
	
	Car findByIdAndIsActive(int carId, boolean isActive);
	
	//Page<Car> findByIsActiveAndPolicyId(boolean isActive, int policyId,Pageable pageable);
	
	//List<Car> findByPolicyId(int policyId);
	
	//Page<Car> findByPolicyIdAndLicensePlateContainingAndIsActive(int policyId,String query,Pageable pageable,boolean isActive);
	
	Page<Car> findByLicensePlateContaining(String query,Pageable pageable);
	
	Page<Car> findByIsActive(boolean isActive, Pageable pageable);
	
	List<Car> findByCustomerId(int custumerId); 
}
