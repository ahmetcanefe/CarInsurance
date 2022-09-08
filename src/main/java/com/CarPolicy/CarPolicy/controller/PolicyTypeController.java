package com.CarPolicy.CarPolicy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.CarPolicy.CarPolicy.dtos.ApplyPolicyDto;
import com.CarPolicy.CarPolicy.dtos.CarDto;
import com.CarPolicy.CarPolicy.dtos.CustomerDto;
import com.CarPolicy.CarPolicy.dtos.PolicyDto;
import com.CarPolicy.CarPolicy.dtos.PolicyTypeDto;
import com.CarPolicy.CarPolicy.entities.Customer;
import com.CarPolicy.CarPolicy.entities.Policy;
import com.CarPolicy.CarPolicy.repositories.CustomerRepository;
import com.CarPolicy.CarPolicy.services.CarService;
import com.CarPolicy.CarPolicy.services.PolicyService;
import com.CarPolicy.CarPolicy.services.PolicyTypeService;
import com.CarPolicy.CarPolicy.utility.SecurityUtils;

@Controller
public class PolicyTypeController {

	private PolicyTypeService policyTypeService;
	private CarService carService;
	private CustomerRepository customerRepository;
	private PolicyService policyService;
	private ModelMapper modelMapper;

	public PolicyTypeController(ModelMapper modelMapper,PolicyTypeService policyTypeService,PolicyService policyService,CarService carService,CustomerRepository customerRepository) {
		super();
		this.policyTypeService = policyTypeService;
		this.carService = carService;
		this.customerRepository = customerRepository;
		this.policyService = policyService;
		this.modelMapper = modelMapper;
	}
	
	
	@GetMapping("/policies/getAll")
	public String getAllPolicies(Model model)
	{
		List<PolicyTypeDto> policyTypeDtos = policyTypeService.getAllPolicyTypes();
		if(policyTypeDtos!=null)
		{
			model.addAttribute("policies",policyTypeDtos);
			return "policy/index";
		}
		return "NotFound";
	}
	
	@GetMapping("/policies/getById/{policyId}")
	public String getPolicyById(@PathVariable int policyId,
			                     Model model)
	{
		PolicyTypeDto policyTypeDto = policyTypeService.getPolicyTypeById(policyId);
		if(policyTypeDto!=null)
		{		
			model.addAttribute("policy",policyTypeDto);
			return "policy/detail";
		}
		return "NotFound";
	}
	
	
	
	@GetMapping("/policies/apply/{policyTypeId}")
	public String applyToPolicy(@PathVariable int policyTypeId, Model model)
	{
		PolicyTypeDto policyTypeDto = policyTypeService.getPolicyTypeById(policyTypeId);
		if(policyTypeDto!=null)
		{
			ApplyPolicyDto applyPolicyDto = new ApplyPolicyDto();
			
			String email = SecurityUtils.getCurrentUser().getUsername();
			Customer customer = customerRepository.findByEmail(email);
			CustomerDto customerDto = modelMapper.map(customer,CustomerDto.class);
			
			applyPolicyDto.setNationalIdentity(customer.getNationalIdentity());
			applyPolicyDto.setPolicyId(policyTypeId);
			
			//List<CarDto> carDtos = carService.getAllCars();
			//List<CarDto> availableCarDtos = new ArrayList<>();

			
			//model.addAttribute("cars", availableCarDtos);
			model.addAttribute("applyPolicyDto", applyPolicyDto);
			return "/policy/apply";
				
		}
		model.addAttribute("message","Policy Not Found For policyId"+policyTypeId);
		return "NotFound";
	}
	
	
	
	@PostMapping("/policies/apply")
	public String applyToPolicy(@Valid @ModelAttribute("applyPolicyDto") ApplyPolicyDto applyPolicyDto,
			                    BindingResult result,
			                    Model model)
	{
		if(!result.hasErrors())
		{
			ApplyPolicyDto checkOutPolicyDto = policyService.addToPolicy(applyPolicyDto.getNationalIdentity(), applyPolicyDto.getLicensePlate(), applyPolicyDto.getPolicyId(), applyPolicyDto);
			if(checkOutPolicyDto!=null)
			{			
				model.addAttribute("policy",checkOutPolicyDto);
			    return "policy/checkout";
			}
		}
		
		List<CarDto> carDtos = carService.getAllCars();
		List<CarDto> applyAvailablePolicyCarDtos = new ArrayList<>();
			
	    	for(CarDto carDto : carDtos)
			{
			   if(carDto.getPolicies()==null) {
				   applyAvailablePolicyCarDtos.add(carDto);   
			   }
			}
	    	
	    model.addAttribute("cars",applyAvailablePolicyCarDtos);
		model.addAttribute(applyPolicyDto);
		return "/policy/apply";
		
	}
	
	@PostMapping("/policies/checkout")
	public String checkoutToPolicy(@Valid @ModelAttribute("applyPolicyDto") ApplyPolicyDto applyPolicyDto,
			                    BindingResult result,
			                    Model model)
	{
		if(!result.hasErrors())
		{
			PolicyDto policyDto = policyService.checkoutToPolicy(applyPolicyDto.getNationalIdentity(), applyPolicyDto.getLicensePlate(), applyPolicyDto.getPolicyId(), applyPolicyDto);
			if(policyDto!=null)
			{			
			    return "redirect:/cars/getAll";
			}
		}
		
		model.addAttribute(applyPolicyDto);
		return "/policy/apply";
		
	}
}
