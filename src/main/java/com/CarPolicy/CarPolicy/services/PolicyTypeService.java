package com.CarPolicy.CarPolicy.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.CarPolicy.CarPolicy.dtos.PolicyDto;
import com.CarPolicy.CarPolicy.dtos.PolicyTypeDto;
import com.CarPolicy.CarPolicy.entities.Policy;
import com.CarPolicy.CarPolicy.entities.PolicyType;
import com.CarPolicy.CarPolicy.repositories.CarRepository;
import com.CarPolicy.CarPolicy.repositories.PolicyRepository;
import com.CarPolicy.CarPolicy.repositories.PolicyTypeRepository;

@Service
public class PolicyTypeService {

	private PolicyTypeRepository policyTypeRepository;
	private PolicyRepository policyRepository;
	private CarRepository carRepository;
	private ModelMapper modelMapper;

	public PolicyTypeService(PolicyRepository policyRepository,PolicyTypeRepository policyTypeRepository,ModelMapper modelMapper,CarRepository carRepository) {
		super();
		this.policyTypeRepository = policyTypeRepository;
		this.modelMapper = modelMapper;
		this.carRepository = carRepository;
		this.policyRepository = policyRepository;
	}
	
	public List<PolicyTypeDto> getAllPolicyTypes()
	{
		List<PolicyType> policyTypes = policyTypeRepository.findAll();
		if(policyTypes!=null)
		{
			List<PolicyTypeDto> policyTypeDtos = policyTypes.stream()
					.map(policy -> modelMapper.map(policy, PolicyTypeDto.class))
					.collect(Collectors.toList());
			
			return policyTypeDtos;
		}
		throw new RuntimeException("policies bulunamadı");
	}
	
	public PolicyTypeDto getPolicyTypeById(int policyTypeId)
	{
		Optional<PolicyType> policyType = policyTypeRepository.findById(policyTypeId);
		if(policyType.isPresent())
		{
			PolicyType foundPolicyType = policyType.get();
			return modelMapper.map(foundPolicyType, PolicyTypeDto.class);
		}		
	    return null;
	}
	
	public PolicyTypeDto addPolicy(PolicyTypeDto policyTypeDto)
	{
		PolicyType policyType = modelMapper.map(policyTypeDto, PolicyType.class);
		
		PolicyType addedPolicyType = policyTypeRepository.save(policyType);
		if(addedPolicyType!=null)
		{
			return modelMapper.map(addedPolicyType, PolicyTypeDto.class);
		}
		return null;
	}
	
	public PolicyTypeDto updatePolicy(int policyTypeId, PolicyTypeDto policyTypeDto)
	{
		Optional<PolicyType> policyType = policyTypeRepository.findById(policyTypeId);
		if(policyType.isPresent())
		{
			PolicyType foundPolicy = policyType.get();
			foundPolicy.setName(policyTypeDto.getName());
			foundPolicy.setDetail(policyTypeDto.getDetail());
			
			PolicyType updatedPolicyType = policyTypeRepository.save(foundPolicy);
			
			return modelMapper.map(updatedPolicyType, PolicyTypeDto.class);
		}
		return null;		
	}
	
	public void deletePolicy(int policyId)
	{
		Optional<PolicyType> policyType = policyTypeRepository.findById(policyId);
		if(policyType.isPresent())
		{	
			policyTypeRepository.delete(policyType.get());
		}
		else {
			throw new RuntimeException("Hata");
		}		
	}
	
	
}
