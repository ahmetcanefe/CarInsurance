package com.CarPolicy.CarPolicy.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.CarPolicy.CarPolicy.dtos.AddCarAccidentDto;
import com.CarPolicy.CarPolicy.dtos.CarAccidentDto;
import com.CarPolicy.CarPolicy.dtos.CarDto;
import com.CarPolicy.CarPolicy.entities.Car;
import com.CarPolicy.CarPolicy.entities.CarAccident;
import com.CarPolicy.CarPolicy.entities.Customer;
import com.CarPolicy.CarPolicy.entities.Role;
import com.CarPolicy.CarPolicy.repositories.CarAccidentRepository;
import com.CarPolicy.CarPolicy.repositories.CarRepository;
import com.CarPolicy.CarPolicy.repositories.CustomerRepository;
import com.CarPolicy.CarPolicy.repositories.RoleRepository;
import com.CarPolicy.CarPolicy.utility.ROLE;
import com.CarPolicy.CarPolicy.utility.SecurityUtils;

@Service
public class CarAccidentService {

	private CarAccidentRepository carAccidentRepository;
    private CarRepository carRepository;
    private ModelMapper modelMapper;
    private CustomerRepository customerRepository;
    private RoleRepository roleRepository;
	
	public CarAccidentService(RoleRepository roleRepository,CustomerRepository customerRepository,CarAccidentRepository carAccidentRepository,CarRepository carRepository,ModelMapper modelMapper) {
		super();
		this.carAccidentRepository = carAccidentRepository;
		this.carRepository = carRepository;
		this.modelMapper = modelMapper;
		this.customerRepository = customerRepository;
		this.roleRepository = roleRepository;
	}
	
	
	public List<CarAccidentDto> getAllByCarId(int carId)
	{
		List<CarAccident> carAccidents = carAccidentRepository.findByCarIdAndIsActive(carId,true);
	    
		List<CarAccidentDto> carAccidentDtos = carAccidents.stream()
					                                 .map((carAccident) -> modelMapper.map(carAccident, CarAccidentDto.class))
					                                 .collect(Collectors.toList());
			return carAccidentDtos;

	}
	
	public CarAccidentDto getById(int carId, int carAccidentId)
	{
		Optional<Car> car = carRepository.findById(carId);
		CarAccident carAccident = carAccidentRepository.findByIdAndIsActive(carAccidentId, true);
		if(car.isPresent() && carAccident!=null && carAccident.getCar()==car.get())
		{
			return modelMapper.map(carAccident, CarAccidentDto.class);
		}
		return null;
	}
	
	public CarAccidentDto addCarAccident(int carId, AddCarAccidentDto carAccidentDto)
	{
		Optional<Car> car = carRepository.findById(carId);
		if(car.isPresent())
		{
			CarAccident carAccident = modelMapper.map(carAccidentDto, CarAccident.class);
			carAccident.setCar(car.get());
			carAccident.setActive(false);
			
			CarAccident addedCarAccident = carAccidentRepository.save(carAccident);
			return modelMapper.map(addedCarAccident, CarAccidentDto.class);
		}
		return null;
	}
	
	public CarAccidentDto updateCarAccident(int carId, int carAccidentId, CarAccidentDto carAccidentDto)
	{
		Optional<Car> car = carRepository.findById(carId);
		Optional<CarAccident> carAccident = carAccidentRepository.findById(carAccidentId);
		if(car.isPresent() && carAccident.isPresent() && carAccident.get().getCar()==car.get())
		{
			CarAccident foundCarAccident = carAccident.get();
			foundCarAccident.setAccidentName(carAccidentDto.getAccidentName());
			foundCarAccident.setAccidentDetail(carAccidentDto.getAccidentDetail());
			foundCarAccident.setDamagePoints(carAccidentDto.getDamagePoints());
			foundCarAccident.setCar(car.get());
			
			CarAccident updateCarAccident = carAccidentRepository.save(foundCarAccident);
			return modelMapper.map(updateCarAccident, CarAccidentDto.class);
		}
		return null;
	}
	
	public void deleteCarAccident(int carId, int carAccidentId)
	{
		Optional<Car> car = carRepository.findById(carId);
		Optional<CarAccident> carAccident = carAccidentRepository.findById(carAccidentId);
		if(car.isPresent() && carAccident.isPresent() && carAccident.get().getCar()==car.get())
		{
			carAccidentRepository.delete(carAccident.get());
		}
		else
		{
			throw new RuntimeException("böyle bir kayıt yok");
		}
		
	}
	
	public List<CarAccidentDto> getAllCarAccidentByNonActive()
	{
		String email = SecurityUtils.getCurrentUser().getUsername();
		Customer customer = customerRepository.findByEmail(email);
		Role role = roleRepository.findByName("ROLE_ADMIN");
		
		List<CarAccident> carAccidents = null;
		
		if(customer.getRoles().contains(role))
		{
			carAccidents = carAccidentRepository.findByIsActive(false);
		}	   
		else
		{	
			carAccidents = carAccidentRepository.findByCar_Customer_IdAndIsActive(customer.getId(),false);
		}	
		
		List<CarAccidentDto> carAccidentDtos = carAccidents.stream()
				                   .map((carAccident) -> modelMapper.map(carAccident, CarAccidentDto.class))
				                   .collect(Collectors.toList());
		return carAccidentDtos;
	}
	
	public Page<CarAccident> getAllCarAccidentsByNonActiveAndPageNumber(int pageNumber)
	{
		Pageable pageable = PageRequest.of(pageNumber-1, 5);
		Page<CarAccident> pageCarAccidents = carAccidentRepository.findByIsActive(false, pageable);
		
		if(pageCarAccidents!=null)
		{
			return pageCarAccidents;
		}
		return null;
	}
	
	public CarAccidentDto approveCar(int carAccidentId)
	{
		Optional<CarAccident> carAccident = carAccidentRepository.findById(carAccidentId);
		if(carAccident.isPresent())
		{
			CarAccident foundCarAccident = carAccident.get();
			foundCarAccident.setActive(true);
			
			CarAccident updatedCarAccident = carAccidentRepository.save(foundCarAccident);
			return modelMapper.map(updatedCarAccident, CarAccidentDto.class);
		}
		else {
			throw new RuntimeException("araba yok");	
		}
	}
	
	public void disApproveCar(int carAccidentId)
	{
		Optional<CarAccident> carAccident = carAccidentRepository.findById(carAccidentId);
		if(carAccident.isPresent())
		{
			CarAccident foundCarAccident = carAccident.get();
			carAccidentRepository.delete(foundCarAccident);
		}
		else {
			throw new RuntimeException("araba yok");	
		}
	}
	
	
}
