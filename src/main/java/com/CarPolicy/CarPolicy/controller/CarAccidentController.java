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

import com.CarPolicy.CarPolicy.dtos.AddCarAccidentDto;
import com.CarPolicy.CarPolicy.dtos.CarAccidentDto;
import com.CarPolicy.CarPolicy.dtos.CarDto;
import com.CarPolicy.CarPolicy.entities.Car;
import com.CarPolicy.CarPolicy.services.CarAccidentService;
import com.CarPolicy.CarPolicy.services.CarService;



@Controller
public class CarAccidentController {

	private CarAccidentService carAccidentService;
	private CarService carService;
	
	
	public CarAccidentController(CarAccidentService carAccidentService,CarService carService) {
		super();
		this.carAccidentService = carAccidentService;
		this.carService = carService;
	}

	
	
	@GetMapping("/{carId}/carAccidents/get")
	public String getCarAccidentByCarId(@PathVariable int carId, Model model)
	{
		List<CarAccidentDto> carAccidentDtos = carAccidentService.getAllByCarId(carId);
	    model.addAttribute("carAccidents",carAccidentDtos);
	    return "car/carAccidents";
	}
	

	@GetMapping("/{carId}/carAccidents")
	public String addCarAccident(@PathVariable int carId, Model model)
	{
		CarDto carDto = carService.getCarById(carId);
		if(carDto!=null)
		{
			CarAccidentDto carAccidentDto = new CarAccidentDto();
			carAccidentDto.carId=carId;
			model.addAttribute("carAccident", carAccidentDto);
			return "car/addCarAccident";
		}
		model.addAttribute("message","Car Not Found for carId"+carId);
		return "NotFound";
		
	}
	
	@PostMapping("/{carId}/carAccidents")
	public String addCarAccident(@PathVariable int carId,
			                    @Valid @ModelAttribute("carAccident") AddCarAccidentDto carAccidentDto,
			                    BindingResult result,
			                    Model model)
	{
		if(!result.hasErrors())
		{
			carAccidentService.addCarAccident(carId,carAccidentDto);
			return "redirect:/cars/history";
		}
		
		carAccidentDto.carId=carId;
		model.addAttribute("carAccident", carAccidentDto);
		return "car/addCarAccident";
	}
	
	@GetMapping("/{carId}/carAccidents/update/{carAccidentId}")
	public String updateCarAccident(@PathVariable("carId") int carId,@PathVariable("carAccidentId") int carAccidentId ,Model model)
	{
		CarAccidentDto carAccidentDto = carAccidentService.getById(carId, carAccidentId);
		if(carAccidentDto!=null)
		{
			carAccidentDto.setCarId(carId);
			model.addAttribute("carAccident",carAccidentDto);
			return "car/updateCarAccident";
		}
		model.addAttribute("message","carAccident Not Found For carAccidentId"+carAccidentId);
		return "NotFound";
	}
	
	@PostMapping("/{carId}/carAccidents/update/{carAccidentId}")
	public String updateCarAccident(@PathVariable("carId") int carId,@PathVariable("carAccidentId") int carAccidentId,
			                        @Valid @ModelAttribute("carAccident") CarAccidentDto carAccidentDto,
			                        BindingResult result,
			                        Model model)
	{
		if(!result.hasErrors())
		{
			CarAccidentDto updatedCarAccidentDto = carAccidentService.updateCarAccident(carId, carAccidentId, carAccidentDto);
			if(updatedCarAccidentDto!=null)
			{
				return "redirect:/cars/getById/"+carId;
			}
		}
		carAccidentDto.setCarId(carId);
		carAccidentDto.setId(carAccidentId);
		model.addAttribute("carAccident",carAccidentDto);
		return "car/updateCarAccident";
	}
	
	@GetMapping("/{carId}/carAccidents/delete/{carAccidentId}")
	public String deleteCarAccident(@PathVariable("carId") int carId, @PathVariable("carAccidentId") int carAccidentId)
	{
		carAccidentService.deleteCarAccident(carId, carAccidentId);
		return "redirect:/cars/getById/"+carId;
	}
	
	@GetMapping("/cars/history")
	public String getCarAccidentHistory(Model model)
	{
		List<CarAccidentDto> carAccidentDtos = carAccidentService.getAllCarAccidentByNonActive(); 
		
	    model.addAttribute("carAccidents",carAccidentDtos);
		return "car/history";
	}
}
