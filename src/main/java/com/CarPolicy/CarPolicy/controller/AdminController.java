package com.CarPolicy.CarPolicy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.CarPolicy.CarPolicy.dtos.CarAccidentDto;
import com.CarPolicy.CarPolicy.dtos.CarDto;
import com.CarPolicy.CarPolicy.dtos.CustomerDto;
import com.CarPolicy.CarPolicy.dtos.ModelDto;
import com.CarPolicy.CarPolicy.dtos.PolicyDto;
import com.CarPolicy.CarPolicy.dtos.PolicyTypeDto;
import com.CarPolicy.CarPolicy.dtos.RoleDto;
import com.CarPolicy.CarPolicy.entities.Car;
import com.CarPolicy.CarPolicy.entities.CarAccident;
import com.CarPolicy.CarPolicy.entities.City;
import com.CarPolicy.CarPolicy.entities.Customer;
import com.CarPolicy.CarPolicy.entities.Policy;
import com.CarPolicy.CarPolicy.entities.Role;
import com.CarPolicy.CarPolicy.services.CarAccidentService;
import com.CarPolicy.CarPolicy.services.CarService;
import com.CarPolicy.CarPolicy.services.CityService;
import com.CarPolicy.CarPolicy.services.CustomerService;
import com.CarPolicy.CarPolicy.services.ModelService;
import com.CarPolicy.CarPolicy.services.PolicyService;
import com.CarPolicy.CarPolicy.services.PolicyTypeService;
import com.CarPolicy.CarPolicy.services.RoleService;

@Controller
public class AdminController {

	private PolicyTypeService policyTypeService;
	private CarService carService;
	private CustomerService customerService;
	private RoleService roleService;
	private CarAccidentService carAccidentService;
	private ModelMapper modelMapper;
	private PolicyService policyService;
	private ModelService modelService;
	private CityService cityService;
	
	
	public AdminController(CityService cityService,ModelService modelService,PolicyService policyService,PolicyTypeService policyTypeService,CarService carService,CustomerService customerService,RoleService roleService,CarAccidentService carAccidentService,ModelMapper modelMapper) {
		super();
		this.policyTypeService = policyTypeService;
		this.carService = carService;
		this.customerService = customerService;
		this.roleService = roleService;
		this.carAccidentService = carAccidentService;
		this.modelMapper = modelMapper;
		this.policyService = policyService;
		this.modelService = modelService;
		this.cityService = cityService;
	}


	@GetMapping("/admin/policies/getAll")
	public String getAllPolicyTypes(Model model)
	{
		List<PolicyTypeDto> policyTypeDtos = policyTypeService.getAllPolicyTypes();
		model.addAttribute("policies",policyTypeDtos);
		return "/admin/index";
	}
	
	
	
	@GetMapping("/admin/policies/add")
	public String addPolicy(Model model)
	{
		PolicyTypeDto policyTypeDto = new PolicyTypeDto();
		model.addAttribute("policy",policyTypeDto);
		return "/admin/addPolicy";
	}
	
	@PostMapping("/admin/policies/add")
	public String addPolicy(@Valid @ModelAttribute("policy") PolicyTypeDto policyTypeDto,
			                 BindingResult result,
			                 Model model)
	{
		if(!result.hasErrors())
		{
			PolicyTypeDto addedPolicyTypeDto = policyTypeService.addPolicy(policyTypeDto);
			if(addedPolicyTypeDto!=null)
			{
				return "redirect:/admin/policies/getAll";
			}
		}
		model.addAttribute(policyTypeDto);
		return "/admin/addPolicy";
	}
	
	@GetMapping("/admin/policies/update/{policyId}")
	public String updatePolicy(@PathVariable int policyId,
			                   Model model)
	{
		PolicyTypeDto policyTypeDto = policyTypeService.getPolicyTypeById(policyId);
		if(policyTypeDto!=null)
		{
			model.addAttribute("policy",policyTypeDto);
			return "/admin/updatePolicy";	
		}	
		model.addAttribute("message","Policy Not Found For policyId_"+policyId);
	    return "NotFound";
	}
	
	@PostMapping("/admin/policies/update/{policyId}")
	public String updatePolicy(@PathVariable int policyId,
			                   @Valid @ModelAttribute("policy") PolicyTypeDto policyTypeDto,
			                   BindingResult result,
			                   Model model)
	{
		if(!result.hasErrors())
		{
			PolicyTypeDto updatedPolicyTypeDto = policyTypeService.updatePolicy(policyId,policyTypeDto);
			if(updatedPolicyTypeDto!=null)
			{
				return "redirect:/admin/policies/getAll";
			}
		}
		policyTypeDto.setId(policyId);
		model.addAttribute(policyTypeDto);
		return "/admin/updatePolicy";
	}
	
	@GetMapping("/admin/policies/delete/{policyId}")
	public String deletePolicy(@PathVariable int policyId)
	{
		policyTypeService.deletePolicy(policyId);
		return "redirect:/admin/policies/getAll";
	}
	
	@GetMapping("/admin/policies/getById/{policyTypeId}/{pageNumber}")
	public String getPolicyTypeById(@PathVariable("policyTypeId") int policyTypeId,
			                    @PathVariable("pageNumber") int currentPage,
			                    Model model)
	{
		PolicyTypeDto policyTypeDto = policyTypeService.getPolicyTypeById(policyTypeId);
		if(policyTypeDto!=null)
		{
            //Page<Car> carPage = carService.getAllCarsByIsActiveAndPolicyId(true, policyTypeId, currentPage);	
            Page<Policy> policyPage = policyService.getAllPoliciesByPolicyTypeId(policyTypeId,currentPage);
			int totalPages = policyPage.getTotalPages();
            long totalItems = policyPage.getTotalElements();
            List<Policy> policies = policyPage.getContent();
            
            List<PolicyDto> policyDtos = policies.stream()
	                   .map((policy) -> modelMapper.map(policy, PolicyDto.class))
	                   .collect(Collectors.toList());
            
            
            model.addAttribute("currentPage",currentPage);
            model.addAttribute("totalPages",totalPages);
            model.addAttribute("totalItems",totalItems);
			model.addAttribute("policies",policyDtos);
			model.addAttribute("policyType",policyTypeDto);
			return "/admin/detail";
		}
		model.addAttribute("message","Policy Not Found For policyId_"+policyTypeId);
	    return "NotFound";
	}
	
	@GetMapping("admin/policyDetail/{policyId}/search/{pageNumber}")
	public String searchCarsByPolicy(@RequestParam(value="query") String query,@PathVariable("policyId") int policyTypeId,@PathVariable("pageNumber") int pageNumber,
            Model model)
     {
		PolicyTypeDto policyTypeDto = policyTypeService.getPolicyTypeById(policyTypeId);
		if(policyTypeDto!=null)
		{
		Page<Policy> policyPage = policyService.searchPoliciesByPolicyTypeId(policyTypeId, query.toUpperCase(), pageNumber);
	    int totalPages = policyPage.getTotalPages();
	    long totalItems = policyPage.getTotalElements();
        
       
        List<Policy> policies = policyPage.getContent();
        
        List<PolicyDto> policyDtos = policies.stream()
                   .map((policy) -> modelMapper.map(policy, PolicyDto.class))
                   .collect(Collectors.toList());
        
        
        model.addAttribute("currentPage",pageNumber);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("totalItems",totalItems);
		model.addAttribute("policies",policyDtos);
		model.addAttribute("policyType",policyTypeDto);
		model.addAttribute("query",query);
		
		return "admin/detailSearch";
		}
		model.addAttribute("message","Policy Not Found For policyId_"+policyTypeId);
	    return "NotFound";
     }
	
	@GetMapping("admin/policies/deletePolicy/{policyId}")
	public String deletePolicy(@PathVariable int policyId,Model model)	                       
	{
		PolicyDto policyDto = policyService.getPolicyById(policyId);
		if(policyDto!=null)
		{
			policyService.deletePolicy(policyId);
			
			model.addAttribute("policy.id",policyId);
			
			return "redirect:/admin/policies/getAll";
		}
		return "NotFound";
	}
	
	

	@GetMapping("/admin/costumers/getAll/{currentPage}")
	public String getAllCostumers(@PathVariable int currentPage, Model model)
	{
		Page<Customer> pageCustomers = customerService.getAllCustomersByPageNumber(currentPage);
	    int totalPages = pageCustomers.getTotalPages();
	    long totalItems = pageCustomers.getTotalElements();
	    List<Customer> customers = pageCustomers.getContent();
		
		List<CustomerDto> customerDtos = customers.stream()
				                             .map(customer -> modelMapper.map(customer, CustomerDto.class))
				                             .collect(Collectors.toList());
		
		model.addAttribute("customers",customerDtos);
		model.addAttribute("currentPage",currentPage);
	    model.addAttribute("totalPages",totalPages);
	    model.addAttribute("totalItems",totalItems);
	
	    return "/admin/customerIndex";
	}
	
	@GetMapping("/admin/costumers/getAll/search/{currentPage}")
	public String searchCustomers(@RequestParam(value="query") String query, @PathVariable int currentPage, Model model)
	{
		Page<Customer> pageCustomers = customerService.searchCustomer(query,currentPage);
	    int totalPages = pageCustomers.getTotalPages();
	    long totalItems = pageCustomers.getTotalElements();
	    List<Customer> customers = pageCustomers.getContent();
		
		List<CustomerDto> customerDtos = customers.stream()
				                             .map(customer -> modelMapper.map(customer, CustomerDto.class))
				                             .collect(Collectors.toList());
		
		model.addAttribute("customers",customerDtos);
		model.addAttribute("currentPage",currentPage);
	    model.addAttribute("totalPages",totalPages);
	    model.addAttribute("totalItems",totalItems);
	    model.addAttribute("query", query);
	
	    return "/admin/customerIndexSearch";
	}
	
	
	
	@GetMapping("/admin/customers/update/{customerId}")
	public String updateCustomer(@PathVariable int customerId,
			                      Model model)
	{
		CustomerDto customerDto = customerService.getCustomerById(customerId);
		if(customerDto!=null)
		{
			List<City> cities = cityService.getAllCities();
			model.addAttribute("cities",cities);
			model.addAttribute("customer",customerDto);
			return "/admin/updateCustomer";
		}
		model.addAttribute("message","Customer Not Found for customerId_"+customerId);
		return "NotFound";
	}
	
	@PostMapping("admin/customers/update/{customerId}")
	public String updateCustomer(@PathVariable int customerId,
			                     @Valid @ModelAttribute("customer") CustomerDto customerDto,
			                     BindingResult result,
			                     Model model
			                     )
	{
		if(!result.hasErrors())
		{
			CustomerDto updatedCustomer = customerService.updateCustomer(customerId, customerDto);
			if(updatedCustomer!=null)
			{
				return "redirect:/admin/costumers/getAll/1";
			}		
		}
		customerDto.setId(customerId);
		List<City> cities = cityService.getAllCities();
		model.addAttribute("cities",cities);
		model.addAttribute(customerDto);
		return "/admin/updateCustomer";
	}
	
	@GetMapping("admin/customers/delete/{customerId}")
	public String deleteCustomer(@PathVariable int customerId)
	{
		customerService.deleteCustomer(customerId);
		return "redirect:/admin/costumers/getAll/1";
	}
	
	@GetMapping("/admin/customers/getById/{customerId}")
	public String getCustomerById(@PathVariable int customerId,
			                       Model model)
	{
		CustomerDto customerDto = customerService.getCustomerById(customerId);
		model.addAttribute("customer",customerDto);
		return "/admin/detailCustomer";
	}
	
	@GetMapping("admin/cars/getAll/{currentPage}")
	public String getAllCars(Model model,@PathVariable int currentPage)
	{
		Page<Car> pageCars = carService.getAllCarsByPageNumber(currentPage);
	    int totalPages = pageCars.getTotalPages();
		long totalItems = pageCars.getTotalElements();
		List<Car> cars = pageCars.getContent();
		
		List<CarDto> carDtos = cars.stream().map(car -> modelMapper.map(car, CarDto.class)).collect(Collectors.toList());
		
		
		model.addAttribute("cars",carDtos);
		model.addAttribute("currentPage",currentPage);
		model.addAttribute("totalPages",totalPages);
	    model.addAttribute("totalItems",totalItems);
	    
		return "admin/cars";	
	}
	
	@GetMapping("admin/cars/getAll/search/{currentPage}")
	public String searchCars(@RequestParam("query") String query, Model model, @PathVariable int currentPage)
	{
		Page<Car> pageCars = carService.searchCar(query.toUpperCase(), currentPage);
	    int totalPages = pageCars.getTotalPages();
		long totalItems = pageCars.getTotalElements();
		List<Car> cars = pageCars.getContent();
		
		List<CarDto> carDtos = cars.stream().map(car -> modelMapper.map(car, CarDto.class)).collect(Collectors.toList());
		
		
		model.addAttribute("cars",carDtos);
		model.addAttribute("currentPage",currentPage);
		model.addAttribute("totalPages",totalPages);
	    model.addAttribute("totalItems",totalItems);
	    model.addAttribute("query",query);
	    
		return "/admin/carsSearch";	
	}
	
	

	
	@GetMapping("/admin/cars/getById/{carId}")
	public String getCarById(@PathVariable int carId, Model model)
	{
		CarDto carDto = carService.getCarById(carId);
		model.addAttribute("car",carDto);
		return "admin/carDetail";
	}
	
	@GetMapping("/admin/{carId}/carAccidents/get")
	public String getCarAccidents(@PathVariable int carId ,Model model)
	{
		List<CarAccidentDto> carAccidentDtos = carAccidentService.getAllByCarId(carId);
	    model.addAttribute("carAccidents",carAccidentDtos);
	    return "admin/carAccidents"; 
	}

	

	
	@GetMapping("admin/customers/get/{customerId}/{carId}")
	public String getCustomerByIdAndCarById(@PathVariable("customerId") int customerId, @PathVariable("carId") int carId,Model model)
	{
		CustomerDto customerDto = customerService.getCustomerById(customerId);
		CarDto carDto = carService.getCarById(carId);
		
		model.addAttribute("customer",customerDto);
		model.addAttribute("car",carDto);
		
		return "admin/moreDetailPolicy";
	}
	

    @GetMapping("admin/models/getAll")
    public String getAllModel(Model model)
    {
    	List<ModelDto> modelDtos = modelService.getAllModels();
    	model.addAttribute("models",modelDtos);
    	return "admin/models";
    }

    @GetMapping("admin/models/add")
    public String addModel(Model model)
    {
    	com.CarPolicy.CarPolicy.entities.Model newModel = new com.CarPolicy.CarPolicy.entities.Model();
		model.addAttribute("model",newModel);
		return "admin/addModel";
    } 
	
    @PostMapping("/admin/models/add")
	public String addModel(@Valid @ModelAttribute("model") ModelDto modelDto,
			                 BindingResult result,
			                 Model model)
	{
		if(!result.hasErrors())
		{
			ModelDto addedModelDto = modelService.addModel(modelDto);
			if(addedModelDto!=null)
			{
				return "redirect:/admin/models/getAll";
			}
		}
		model.addAttribute(modelDto);
		return "admin/addModel";
	}
	
    @GetMapping("/admin/models/update/{modelId}")
	public String updateModel(@PathVariable int modelId,
			                      Model model)
	{
		ModelDto modelDto = modelService.getById(modelId);
		if(modelDto!=null)
		{
			model.addAttribute("model",modelDto);
			return "/admin/updateModel";
		}
		model.addAttribute("message","Model Not Found for modeId_"+modelId);
		return "NotFound";
	}
	
	@PostMapping("admin/models/update/{modelId}")
	public String updateModel(@PathVariable int modelId,
			                     @Valid @ModelAttribute("model") ModelDto modelDto,
			                     BindingResult result,
			                     Model model
			                     )
	{
		if(!result.hasErrors())
		{
			ModelDto updatedModel = modelService.updateModel(modelId,modelDto);
			if(updatedModel!=null)
			{
				return "redirect:/admin/models/getAll";
			}		
		}
		model.addAttribute(modelDto);
		return "/admin/updateModel";
	}
	
	
	@GetMapping("/admin/history")
	public String getAdminHistory(Model model)
	{
        List<CarAccidentDto> carAccidentDtos = carAccidentService.getAllCarAccidentByNonActive(); 
		
	    model.addAttribute("carAccidents",carAccidentDtos);
		return "admin/history";
	}
	
	@GetMapping("/admin/approve/{carAccidentId}")
	public String approveCar(@PathVariable int carAccidentId)
	{
		CarAccidentDto carAccidentDto = carAccidentService.approveCar(carAccidentId);
	    return "redirect:/admin/history";
	}
	
	@GetMapping("/admin/disapprove/{carAccidentId}")
	public String disapproveCar(@PathVariable int carAccidentId)
	{
		carAccidentService.disApproveCar(carAccidentId);
		return "redirect:/admin/history";
	}
	
	
	
	
	
	
	
}
