package com.CarPolicy.CarPolicy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.CarPolicy.CarPolicy.dtos.ApplyPolicyDto;
import com.CarPolicy.CarPolicy.dtos.CarAccidentDto;
import com.CarPolicy.CarPolicy.dtos.CarDto;
import com.CarPolicy.CarPolicy.dtos.PolicyDto;
import com.CarPolicy.CarPolicy.entities.Car;
import com.CarPolicy.CarPolicy.entities.Customer;
import com.CarPolicy.CarPolicy.repositories.CustomerRepository;
import com.CarPolicy.CarPolicy.services.CarService;
import com.CarPolicy.CarPolicy.services.PolicyService;
import com.CarPolicy.CarPolicy.utility.SecurityUtils;

@Controller
public class PolicyController {

	private PolicyService policyService;
	
	public PolicyController(PolicyService policyService) {
		super();
		this.policyService = policyService;
	}

	
	@GetMapping("/{carId}/policies/get")
	public String getpoliciesByCarId(@PathVariable int carId, Model model)
	{
		List<PolicyDto> policyDtos = policyService.getAllByCarId(carId);
	    model.addAttribute("policies",policyDtos);
	    return "car/policies";
	}
	
	
	
	
	
	
	
	
	
	
}
