package com.CarPolicy.CarPolicy.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CarPolicy.CarPolicy.entities.CarAccident;

public interface CarAccidentRepository extends JpaRepository<CarAccident, Integer> {

	public List<CarAccident> findByCarId(int carId);
	
	public List<CarAccident> findByIsActive(boolean isActive);
	
	Page<CarAccident> findByIsActive(boolean isActive,Pageable pageable);
	
	List<CarAccident> findByCarIdAndIsActive(int carId, boolean isActive);
	
	List<CarAccident> findByCar_Customer_IdAndIsActive(int customerId, boolean isActive);
	
	CarAccident findByIdAndIsActive(int carAccidentId, boolean isActive);
}
