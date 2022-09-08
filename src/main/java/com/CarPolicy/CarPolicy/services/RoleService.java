package com.CarPolicy.CarPolicy.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.CarPolicy.CarPolicy.dtos.RoleDto;
import com.CarPolicy.CarPolicy.entities.Role;
import com.CarPolicy.CarPolicy.repositories.RoleRepository;

@Service
public class RoleService {

	private RoleRepository roleRepository;
	private ModelMapper modelMapper;

	public RoleService(RoleRepository roleRepository,ModelMapper modelMapper) {
		super();
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}
	
	
	public List<RoleDto> getAllRoles()
	{
		List<Role> roles = roleRepository.findAll();
		List<RoleDto> roleDtos = roles.stream()
				                        .map(role -> modelMapper.map(role, RoleDto.class))
				                        .collect(Collectors.toList());
		
		return roleDtos;
	}
	
	public Role getByName(String name)
	{
		Role role = roleRepository.findByName(name);
		if(role!=null)
		{
			return role;
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
}
