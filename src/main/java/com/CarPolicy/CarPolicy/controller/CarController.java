package com.CarPolicy.CarPolicy.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.CarPolicy.CarPolicy.dtos.CarDto;
import com.CarPolicy.CarPolicy.dtos.ModelDto;
import com.CarPolicy.CarPolicy.entities.Car;
import com.CarPolicy.CarPolicy.services.CarService;
import com.CarPolicy.CarPolicy.services.ModelService;
import com.CarPolicy.CarPolicy.utility.DataResult;

@Controller
public class CarController {

	private CarService carService;
	private ModelService modelService;

	public CarController(CarService carService,ModelService modelService) {
		super();
		this.carService = carService;
		this.modelService = modelService;
	}
	
	
	@GetMapping("/cars/getAll")
	public String getAllCars(Model model)
	{
		List<CarDto> carDtos = carService.getAllCars();
	
	    model.addAttribute("cars", carDtos);
		return "/car/index";
		
	}
	
	@GetMapping("/cars/getById/{carId}")
	public String getCarById(@PathVariable int carId, Model model)
	{
		CarDto carDto = carService.getCarById(carId);
		model.addAttribute("car",carDto);
		return "car/detail";
	}
	
	@GetMapping("/cars/add")
	public String addCar(Model model)
	{
		CarDto carDto = new CarDto();
		List<ModelDto> modelDtos = modelService.getAllModels();
		
		model.addAttribute("car",carDto);
		model.addAttribute("models",modelDtos);
		return "car/add";
	}
	
	@PostMapping("/cars/add")
	public String addCar(@Valid @ModelAttribute("car") CarDto carDto,
			             BindingResult result,
			             Model model)
	{
		if(!result.hasErrors())
		{
			Car existingCar = carService.getByLicensePlateAndCustomerId(carDto.getLicensePlate());
			if(existingCar!=null) {
				result.rejectValue("licensePlate", null, "There is already a car with same license plate");
			}
			else 
			{
				CarDto addedCarDto = carService.addCar(carDto);
				if(addedCarDto!=null)
				{
					return "redirect:/cars/getAll";
				}	
			}    
		}
		List<ModelDto> modelDtos = modelService.getAllModels();
		model.addAttribute("car", carDto);
		model.addAttribute("models",modelDtos);
		return "car/add";
	}
	
	@GetMapping("/cars/update/{carId}")
	public String updateCar(@PathVariable int carId, Model model)
	{
		CarDto carDto = carService.getCarById(carId);
		if(carDto!=null)
		{
			List<ModelDto> modelDtos = modelService.getAllModels();
			
			model.addAttribute("car", carDto);
			model.addAttribute("models",modelDtos);
		    return "car/update";
		}
		model.addAttribute("message","Car is not found for carId_"+carId);
        return "NotFound";
		
	}
	
	@PostMapping("/cars/update/{carId}")
	public String updateCar(@PathVariable int carId,
			                @Valid @ModelAttribute("car") CarDto carDto,
                            BindingResult result,
                            Model model)
	{
		if(!result.hasErrors())
		{
			CarDto updatedCarDto = carService.updateCar(carId, carDto);
			if(updatedCarDto!=null)
			{
				return "redirect:/cars/getAll";
			}
		}		
	    carDto.setId(carId);
	    carDto.setModel(modelService.getById(carDto.getModelId()));
	    List<ModelDto> modelDtos = modelService.getAllModels();
	    
	    model.addAttribute(carDto);
	    model.addAttribute("models",modelDtos);
	    return "car/update";
	}	
	
	@GetMapping("/cars/delete/{carId}")
	public String deleteCar(@PathVariable int carId)
	{
		carService.deleteCar(carId);
		return "redirect:/cars/getAll";
	}
	
	
	
	
	

	
}
