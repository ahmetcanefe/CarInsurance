package com.CarPolicy.CarPolicy.services;

import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.joda.time.Days;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.CarPolicy.CarPolicy.dtos.ApplyPolicyDto;
import com.CarPolicy.CarPolicy.dtos.CarAccidentDto;
import com.CarPolicy.CarPolicy.dtos.CarDto;
import com.CarPolicy.CarPolicy.entities.Car;
import com.CarPolicy.CarPolicy.entities.CarAccident;
import com.CarPolicy.CarPolicy.entities.Customer;
import com.CarPolicy.CarPolicy.entities.Model;
import com.CarPolicy.CarPolicy.entities.Policy;
import com.CarPolicy.CarPolicy.repositories.CarRepository;
import com.CarPolicy.CarPolicy.repositories.CustomerRepository;
import com.CarPolicy.CarPolicy.repositories.ModelRepository;
import com.CarPolicy.CarPolicy.repositories.PolicyRepository;
import com.CarPolicy.CarPolicy.utility.DataResult;
import com.CarPolicy.CarPolicy.utility.ErrorDataResult;
import com.CarPolicy.CarPolicy.utility.ROLE;
import com.CarPolicy.CarPolicy.utility.SecurityUtils;
import com.CarPolicy.CarPolicy.utility.SuccessDataResult;


@Service
public class CarService {

	private CarRepository carRepository;
	private CustomerRepository customerRepository;
	private PolicyRepository policyRepository;
	private ModelMapper modelMapper;
	private ModelRepository modelRepository;

	public CarService(CarRepository carRepository,ModelRepository modelRepository,ModelMapper modelMapper,CustomerRepository customerRepository,PolicyRepository policyRepository) {
		super();
		this.carRepository = carRepository;
		this.modelMapper = modelMapper;
		this.customerRepository = customerRepository;
		this.policyRepository = policyRepository;
		this.modelRepository = modelRepository;
	}
	
	public List<CarDto> getAllCars()
	{
		String role = SecurityUtils.getRole();
		List<Car> cars = null;
		
		if(ROLE.ROLE_ADMIN.name().equals(role))
		{
			cars = carRepository.findAll();
		}	   
		else
		{
			String email = SecurityUtils.getCurrentUser().getUsername();
			Customer customer = customerRepository.findByEmail(email);
			
			cars = carRepository.findByCustomerIdAndIsActive(customer.getId(),true);
		}
		
		List<CarDto> carDtos = cars.stream()
                .map((car) -> modelMapper.map(car, CarDto.class))
                .collect(Collectors.toList());

        return carDtos;
	}

	
	public Page<Car> getAllCarsByPageNumber(int pageNumber)
	{
		
        Pageable pageable = PageRequest.of(pageNumber-1, 10);
		
		Page<Car> pageCars = carRepository.findAll(pageable);
		
        return pageCars;
	}
	
	public CarDto getCarById(int carId)
	{
		Optional<Car> car = carRepository.findById(carId);
		if(car.isPresent())
		{
			return modelMapper.map(car.get(), CarDto.class);
		}		
	    return null;	
	}
	
	public Car getByLicensePlate(String licensePlate)
	{
		Car car = carRepository.findByLicensePlate(licensePlate);
		if(car!=null)
		{
			return car;
		}
		return null;
	}
	
	public CarDto addCar(CarDto carDto)
	{
		String email = SecurityUtils.getCurrentUser().getUsername();
		Customer customer = customerRepository.findByEmail(email);
		
		if(customer!=null)
		{
			Model model = modelRepository.getById(carDto.getModelId());
			
			Car car = new Car();
			car.setModel(model);
			car.setCustomer(customer);
			car.setActive(true);
			car.setLicensePlate(carDto.getLicensePlate().toUpperCase());
			car.setColor(carDto.getColor().toUpperCase());
			car.setManufacturingYear(carDto.getManufacturingYear());
			car.setMileage(carDto.getMileage());
		
			//car.setModel(carDto.getModel().toUpperCase());
			
			Car addedCar = carRepository.save(car);
			return modelMapper.map(addedCar, CarDto.class);
		}
		return null;
		
	}
	
	public CarDto updateCar(int carId, CarDto carDto)
	{
		Optional<Car> car = carRepository.findById(carId);
		if(car.isPresent())
		{
			String email = SecurityUtils.getCurrentUser().getUsername();
			Customer customer = customerRepository.findByEmail(email);
			
			if(customer!=null)
			{
			   Model model = modelRepository.getById(carDto.getModelId());
				
			   Car foundCar = car.get();
			   foundCar.setLicensePlate(carDto.getLicensePlate().toUpperCase());
			   foundCar.setManufacturingYear(carDto.getManufacturingYear());
			   foundCar.setMileage(carDto.getMileage());
			   foundCar.setModel(model);
			   foundCar.setColor(carDto.getColor().toUpperCase());
		       foundCar.setCustomer(customer);
		    
			   Car updatedCar = carRepository.save(foundCar);
			
			   return modelMapper.map(updatedCar, CarDto.class);
			}
		}
		return null;
	}
	
	public void deleteCar(int carId)
	{
		Optional<Car> car = carRepository.findById(carId);
		if(car.isPresent())
		{
			List<Policy> policies = policyRepository.findByCarId(carId);
			policyRepository.deleteAll(policies);
			
			carRepository.deleteById(carId);
		}
	}
	
	public CarDto addToPolicy(String nationalIdentity, String licensePlate, int policyId, ApplyPolicyDto applyPolicyDto)
	{
		Customer customer = customerRepository.findByNationalIdentity(nationalIdentity);		
		Car car = carRepository.findByLicensePlate(licensePlate);
		Optional<Policy> policy = policyRepository.findById(policyId);
		
		if(car.getCustomer()==customer && policy.isPresent())
		{
			//if(car.getPolicy() == null)
			
				long diffInMillies = applyPolicyDto.getEndDate().getTime() - applyPolicyDto.getStartDate().getTime();
			    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

			    double price = 0;
			    if(car.getManufacturingYear()>1950 && car.getManufacturingYear()<=2000)
			    {
			    	price = diff*5.5;
			    }
			    if(car.getManufacturingYear()>2000 && car.getManufacturingYear()<=2005)
			    {
			    	price = diff*5;
			    }
			    if(car.getManufacturingYear()>2005 && car.getManufacturingYear()<=2010)
			    {
			    	price = diff*4.5;
			    }
			    if(car.getManufacturingYear()>2010 && car.getManufacturingYear()<=2015)
			    {
			    	price = diff*4;
			    }
			    if(car.getManufacturingYear()>2015)
			    {
			    	price = diff*3.75;
			    }
			    
				if(price>0)
				{		
					//car.setStartDate(applyPolicyDto.getStartDate());
					//car.setEndDate(applyPolicyDto.getEndDate());				
				    //car.setPrice(price);
				    //car.setPolicy(policy.get());				
					//car.setActive(false);
					
					Car updatedCar = carRepository.save(car);
					return modelMapper.map(updatedCar, CarDto.class);
				}	
			
		}
		return null;
	}
	
	public List<CarDto> getAllCarsByNonActive()
	{
		String role = SecurityUtils.getRole();
		List<Car> cars = null;
		
		if(ROLE.ROLE_ADMIN.name().equals(role))
		{
		    //cars = carRepository.findByIsActive(false);
		}
		else
		{
			String email = SecurityUtils.getCurrentUser().getUsername();
			Customer customer = customerRepository.findByEmail(email);
			
			//cars = carRepository.findByCustomerIdAndIsActive(customer.getId(),false);
		}
		
		List<CarDto> carDtos = cars.stream()
				                   .map((car) -> modelMapper.map(car, CarDto.class))
				                   .collect(Collectors.toList());
		return carDtos;	
	}
	
	public Page<Car> getAllCarsByNonActiveAndPageNumber(int pageNumber)
	{
		Pageable pageable = PageRequest.of(pageNumber-1, 5);
		//Page<Car> pageCars = carRepository.findByIsActive(false, pageable);
		
		//if(pageCars!=null)
		//{
			//return pageCars;
		//}
		return null;
	}
	
	
	
	
	
	
	
	public void deleteFromPolicy(int carId)
	{
		Optional<Car> car = carRepository.findById(carId);
		if(car.isPresent())
		{
			Car foundCar = car.get();
			//foundCar.setPolicy(null);
			//foundCar.setStartDate(null);
			//foundCar.setEndDate(null);
			//foundCar.setPrice(0);
			//foundCar.setActive(true);
			
			
			carRepository.save(foundCar);
		}
		else {
			throw new RuntimeException("araba yok");	
		}
	}
	
//	public Page<Car> searchCarByPolicyId(int policyId,String query, int pageNumber)
//	{
//		Pageable pageable = PageRequest.of(pageNumber-1, 5);
//		Page<Car> pageCars = carRepository.findByPolicyIdAndLicensePlateContainingAndIsActive(policyId, query, pageable, true);
//		
//		if(pageCars!=null)
//		{
//			return pageCars;
//		}
//		return null;
//	}
	
	public Page<Car> searchCar(String query, int pageNumber)
	{
		Pageable pageable = PageRequest.of(pageNumber-1, 5);
		Page<Car> pageCars = carRepository.findByLicensePlateContaining(query, pageable);
		
		if(pageCars!=null)
		{
			return pageCars;
		}
		return null;
	}
	
	
	public Car getByLicensePlateAndCustomerId(String licensePlate)
	{
		String email = SecurityUtils.getCurrentUser().getUsername();
		Customer customer = customerRepository.findByEmail(email);
		
		Car car = carRepository.findByLicensePlateAndCustomerId(licensePlate, customer.getId());
		if(car!=null)
		{
			return car;
		}
		return null;
	}
	
	
	
	
	
	
	
	
}
