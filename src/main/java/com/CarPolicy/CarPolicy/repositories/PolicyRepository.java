package com.CarPolicy.CarPolicy.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CarPolicy.CarPolicy.entities.Policy;
import com.CarPolicy.CarPolicy.entities.PolicyType;

public interface PolicyRepository extends JpaRepository<Policy,Integer>{

	Policy findByCar_CustomerIdAndCarIdAndPolicyType(int customerId, int carId, PolicyType policyType);
	
	Page<Policy> findByPolicyTypeId(int policyTypeId, Pageable pageable);
	
	List<Policy> findByCarId(int carId);
	
	Page<Policy> findByPolicyTypeIdAndCar_LicensePlateContaining(int policyTypeId, String query, Pageable pageable);
}
