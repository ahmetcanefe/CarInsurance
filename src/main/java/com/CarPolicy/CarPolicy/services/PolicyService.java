package com.CarPolicy.CarPolicy.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.CarPolicy.CarPolicy.dtos.ApplyPolicyDto;
import com.CarPolicy.CarPolicy.dtos.CarDto;
import com.CarPolicy.CarPolicy.dtos.PolicyDto;
import com.CarPolicy.CarPolicy.entities.Car;
import com.CarPolicy.CarPolicy.entities.CarAccident;
import com.CarPolicy.CarPolicy.entities.Customer;
import com.CarPolicy.CarPolicy.entities.Policy;
import com.CarPolicy.CarPolicy.entities.PolicyType;
import com.CarPolicy.CarPolicy.repositories.CarRepository;
import com.CarPolicy.CarPolicy.repositories.CustomerRepository;
import com.CarPolicy.CarPolicy.repositories.PolicyRepository;
import com.CarPolicy.CarPolicy.repositories.PolicyTypeRepository;

@Service
public class PolicyService {

	private CarRepository carRepository;
	private CustomerRepository customerRepository;
	private PolicyRepository policyRepository;
	private PolicyTypeRepository policyTypeRepository;
	private ModelMapper modelMapper;

	public PolicyService(CarRepository carRepository,PolicyTypeRepository policyTypeRepository,ModelMapper modelMapper,CustomerRepository customerRepository,PolicyRepository policyRepository) {
		super();
		this.carRepository = carRepository;
		this.modelMapper = modelMapper;
		this.customerRepository = customerRepository;
		this.policyRepository = policyRepository;
		this.policyTypeRepository = policyTypeRepository;
	}
	
	public ApplyPolicyDto addToPolicy(String nationalIdentity, String licensePlate, int policyId, ApplyPolicyDto applyPolicyDto)
	{
		Customer customer = customerRepository.findByNationalIdentity(nationalIdentity);		
		Car car = carRepository.findByLicensePlateAndCustomerId(licensePlate,customer.getId());
		Optional<PolicyType> policyType = policyTypeRepository.findById(policyId);
		Policy policy = policyRepository.findByCar_CustomerIdAndCarIdAndPolicyType(customer.getId(), car.getId(),policyType.get());
		
		if(car!=null && car.getCustomer() == customer && policyType.isPresent() && policy==null)
		{	
				long diffInMillies = applyPolicyDto.getEndDate().getTime() - applyPolicyDto.getStartDate().getTime();
			    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                
			    double price = policyType.get().getMinPrice();
			    
                double rate = car.getCustomer().getCity().getRate();                
                
                if(car.getManufacturingYear()>1950 && car.getManufacturingYear()<=1970)
                {
                	price = price*diff*3*rate;
                }
                else if(car.getManufacturingYear()>1970 && car.getManufacturingYear()<=1990)
                {
                	price = price*diff*3.5*rate;
                }
                else if(car.getManufacturingYear()>1990 && car.getManufacturingYear()<=2000)
                {
                	price = price*diff*4*rate;
                }
                else if(car.getManufacturingYear()>2000 && car.getManufacturingYear()<=2005)
                {
                	price = price*diff*4.5*rate;
                }
                else if(car.getManufacturingYear()>2005 && car.getManufacturingYear()<=2010)
                {
                	price = price*diff*5*rate;
                }
                else if(car.getManufacturingYear()>2010 && car.getManufacturingYear()<=2015)
                {
                	price = price*diff*5.5*rate;
                }
                else if(car.getManufacturingYear()>2015 && car.getManufacturingYear()<=2020)
                {
                	price = price*diff*6*rate;
                }
                else if(car.getManufacturingYear()>2020)
                {
                	price = price*diff*6.5*rate;
                }
           
                if(price > policyType.get().getMinPrice())
                {
                	applyPolicyDto.setPrice(price);    
    				return applyPolicyDto;
                }
		}
		return null;
	}
     
	public PolicyDto checkoutToPolicy(String nationalIdentity, String licensePlate, int policyId, ApplyPolicyDto applyPolicyDto)
	{
		Customer customer = customerRepository.findByNationalIdentity(nationalIdentity);		
		Car car = carRepository.findByLicensePlateAndCustomerId(licensePlate,customer.getId());
		Optional<PolicyType> policyType = policyTypeRepository.findById(policyId);
		Policy policy = policyRepository.findByCar_CustomerIdAndCarIdAndPolicyType(customer.getId(), car.getId(),policyType.get());
		
		if(car!=null && car.getCustomer()== customer && policyType.isPresent() && policy==null)
		{
			Policy newPolicy = new Policy();
			newPolicy.setCar(car);
			newPolicy.setPolicyType(policyType.get());
			newPolicy.setStartDate(applyPolicyDto.getStartDate());
			newPolicy.setEndDate(applyPolicyDto.getEndDate());
			newPolicy.setPrice(applyPolicyDto.getPrice());
			
			Policy addedPolicy = policyRepository.save(newPolicy);
			return modelMapper.map(addedPolicy, PolicyDto.class);
		}
		return null;
}
	
	public void deletePolicy(int policyId)
	{
		Optional<Policy> policy = policyRepository.findById(policyId);
		if(policy.isPresent())
		{
			policyRepository.delete(policy.get());
		}
		else
		{
			throw new RuntimeException("böyle bir kayıt yok");
		}
	}
	
	public PolicyDto getPolicyById(int policyId)
	{
		Optional<Policy> policy = policyRepository.findById(policyId);
		if(policy!=null)
		{
			return modelMapper.map(policy, PolicyDto.class);
		}
		return null;
	}
	
	public Page<Policy> getAllPoliciesByPolicyTypeId(int policyTypeId, int pageNumber)
	{
		Pageable pageable = PageRequest.of(pageNumber-1, 5);
		Page<Policy> policyPage = policyRepository.findByPolicyTypeId(policyTypeId, pageable);
        
		return policyPage;
	}
	
	public List<PolicyDto> getAllByCarId(int carId)
	{
		List<Policy> policies = policyRepository.findByCarId(carId);
		
		List<PolicyDto> policyDtos = policies.stream().map((policy) -> modelMapper.map(policy, PolicyDto.class)).collect(Collectors.toList());
	    if(policyDtos!=null)
		return policyDtos;
	    
	    return null;
	}
	
	public Page<Policy> searchPoliciesByPolicyTypeId(int policyTypeId, String query, int pageNumber)
	{
		Pageable pageable = PageRequest.of(pageNumber-1, 5);
        Page<Policy> pagePolicies = policyRepository.findByPolicyTypeIdAndCar_LicensePlateContaining(policyTypeId,query,pageable);
        return pagePolicies;
	}
	
	
	
}
