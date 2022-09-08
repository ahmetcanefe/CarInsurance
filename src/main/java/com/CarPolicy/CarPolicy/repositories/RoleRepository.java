package com.CarPolicy.CarPolicy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CarPolicy.CarPolicy.entities.Role;

public interface RoleRepository extends JpaRepository<Role,Integer>{

	Role findByName(String name);
}