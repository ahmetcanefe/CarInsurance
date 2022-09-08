package com.CarPolicy.CarPolicy.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.CarPolicy.CarPolicy.dtos.CustomerDto;
import com.CarPolicy.CarPolicy.dtos.RegistrationDto;
import com.CarPolicy.CarPolicy.dtos.RoleDto;
import com.CarPolicy.CarPolicy.entities.Car;
import com.CarPolicy.CarPolicy.entities.City;
import com.CarPolicy.CarPolicy.entities.Customer;
import com.CarPolicy.CarPolicy.entities.Policy;
import com.CarPolicy.CarPolicy.entities.Role;
import com.CarPolicy.CarPolicy.repositories.CarRepository;
import com.CarPolicy.CarPolicy.repositories.CityRepository;
import com.CarPolicy.CarPolicy.repositories.CustomerRepository;
import com.CarPolicy.CarPolicy.repositories.PolicyRepository;
import com.CarPolicy.CarPolicy.repositories.RoleRepository;
import com.CarPolicy.CarPolicy.utility.DataResult;


@Service
public class CustomerService {

	public  CustomerRepository customerRepository;
	public  RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper; 
    private CityRepository cityRepository;
    private CarRepository carRepository;
    private PolicyRepository policyRepository;
	
	public CustomerService(PolicyRepository policyRepository,CarRepository carRepository,CityRepository cityRepository,CustomerRepository customerRepository, RoleRepository roleRepository,PasswordEncoder passwordEncoder,ModelMapper modelMapper) {
		super();
		this.customerRepository = customerRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
		this.cityRepository = cityRepository;
		this.carRepository = carRepository;
		this.policyRepository = policyRepository;
	}
	
	
	
	public List<CustomerDto> getAllCustomers()
	{
		List<Customer> customers = customerRepository.findAll();
		List<Customer> guestCustomers = new ArrayList<>();
		
		Role role = roleRepository.findByName("ROLE_ADMIN");
		
		for(Customer customer : customers)
		{
			if(!customer.getRoles().contains(role))
			{
				guestCustomers.add(customer);
			}
		}
		
		List<CustomerDto> customerDtos = guestCustomers.stream()
				                             .map(customer -> modelMapper.map(customer, CustomerDto.class))
				                             .collect(Collectors.toList());
		
		return customerDtos;
	}
	
	public Page<Customer> getAllCustomersByPageNumber(int pageNumber)
	{
		Pageable pageable = PageRequest.of(pageNumber-1, 5);
		
		Page<Customer> pageCustomers = customerRepository.findByRoles_Name("ROLE_GUEST",pageable);	
		
		return pageCustomers;
	}
	
	public CustomerDto getCustomerById(int customerId)
	{
		Optional<Customer> customer = customerRepository.findById(customerId);
		if(customer.isPresent())
		{
			Customer foundCustomer = customer.get();
			return modelMapper.map(foundCustomer, CustomerDto.class);
		}
		return null;
	}
	
	public Customer findByEmail(String email)
	{
		return customerRepository.findByEmail(email);
	}
	
	public Customer findByNationalNumber(String nationalNumber)
	
	{
		return customerRepository.findByNationalIdentity(nationalNumber);
	}
	
	public void saveUser(RegistrationDto registrationDto)
	{
			Customer customer = modelMapper.map(registrationDto, Customer.class);
			
			Optional<City> city = cityRepository.findById(registrationDto.getCityId());
			if(city.isPresent())
			{
				customer.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
				customer.setCity(city.get());
					
				Role role = roleRepository.findByName("ROLE_GUEST");
				Role role2 = roleRepository.findByName("ROLE_ADMIN");
				
				customer.setRoles(Arrays.asList(role));	
				//customer.setRoles(Arrays.asList(role2));
				customerRepository.save(customer);
			}	
	}
	
	
	
	public CustomerDto updateCustomer(int customerId, CustomerDto customerDto)
	{
		Optional<Customer> customer = customerRepository.findById(customerId);
		if(customer.isPresent())
		{
			Optional<City> city = cityRepository.findById(customerDto.getCityId());
			if(city.isPresent())
			{
				Customer foundCustomer = customer.get();
				foundCustomer.setName(customerDto.getName());
				foundCustomer.setSurname(customerDto.getSurname());
				foundCustomer.setMobilePhone(customerDto.getMobilePhone());
				foundCustomer.setCity(city.get());
				//foundCustomer.setAddress(customerDto.getAddress());
			
				//foundCustomer.setRoles(null);
				
				//for(int id : customerDto.getRoleIds())
				//{
					//Role role = roleRepository.findById(id).orElse(null);
					//if(role!=null)
					//{
						//foundCustomer.setRoles(Arrays.asList(role));
					//}
				//}
			
				Customer updatedCustomer = customerRepository.save(foundCustomer);
				return modelMapper.map(updatedCustomer, CustomerDto.class);
			}
			throw new RuntimeException("hata");
			
		}
		throw new RuntimeException("hata");
	}
	
	
	public void deleteCustomer(int customerId)
	{
		Optional<Customer> customer = customerRepository.findById(customerId);
		if(customer.isPresent())
		{
			Customer foundCustomer = customer.get();
			foundCustomer.setRoles(null);
			
			List<Policy> policies = new ArrayList<>();
			List<Car> cars = carRepository.findByCustomerId(customerId);
			for(Car car : cars)
			{
				policies.addAll(policyRepository.findByCarId(car.getId())); 
			}
			
			policyRepository.deleteAll(policies);
			carRepository.deleteAll(cars);
			
			customerRepository.delete(foundCustomer);
		}
		else {
			throw new RuntimeException("hata");
		}
		
	}
	
	public Page<Customer> searchCustomer(String query, int pageNumber)
	{
		Pageable pageable = PageRequest.of(pageNumber-1, 5);
		Page<Customer> pageCustomers = customerRepository.findByRoles_NameAndNameContainingIgnoreCase("ROLE_GUEST",query, pageable);
		
		if(pageCustomers!=null)
		{
			return pageCustomers;
		}
		return null;
	}
	

	
}

