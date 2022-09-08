package com.CarPolicy.CarPolicy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.CarPolicy.CarPolicy.dtos.RegistrationDto;
import com.CarPolicy.CarPolicy.entities.City;
import com.CarPolicy.CarPolicy.entities.Customer;
import com.CarPolicy.CarPolicy.services.CityService;
import com.CarPolicy.CarPolicy.services.CustomerService;

@Controller
public class AuthController {

	private CustomerService customerService;
	private CityService cityService;
	
	public AuthController(CustomerService customerService,CityService cityService) {
		super();
		this.customerService = customerService;
		this.cityService = cityService;
	}

	
	@GetMapping("/register")
	public String showRegistrationForm(Model model)
	{
		RegistrationDto user = new RegistrationDto();
		List<City> cities = cityService.getAllCities();
		
		model.addAttribute("user",user);
		model.addAttribute("cities",cities);
		return "register";
	}
	
	@PostMapping("/register/save")
	public String register(@Valid @ModelAttribute("user") RegistrationDto user,
			                BindingResult result,
			                Model model)
	{
		
		if(!result.hasErrors())
		{
			Customer existingUserByEmail = customerService.findByEmail(user.getEmail());
			Customer existingUserByNationalNumber = customerService.findByNationalNumber(user.getNationalIdentity());
			if(existingUserByEmail!=null) {
				result.rejectValue("email", null, "There is already a user with same email id");
			}
			else if(existingUserByNationalNumber!=null)
			{
				result.rejectValue("nationalIdentity",null,"There is already a user with same National Identity");
			}
			else {
				customerService.saveUser(user);
				return "redirect:/register?success";
			}
		}
		List<City> cities = cityService.getAllCities();
		model.addAttribute("user", user);
		model.addAttribute("cities",cities);
		return "/register";
		
	}
	
	@GetMapping("/login")
	public String loginPage()
	{
		return "/login";
	}	
	
	@GetMapping("/")
	public String HomePage() 
	{ 
        return "home";
    }
	
	@GetMapping("/default")
	public String defaultAfterLogin(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/admin/policies/getAll";
        }
        return "redirect:/cars/getAll";
    }
	
	
	
	
	
	
	
}
