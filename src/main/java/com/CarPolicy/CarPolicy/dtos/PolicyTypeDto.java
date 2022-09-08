package com.CarPolicy.CarPolicy.dtos;


import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyTypeDto {
	
	    private int id;	
	   
	    @NotEmpty()
	    @Size(min=3, max=20)
		private String name;
		
	    @NotEmpty()
	    @Size(min=3, max=50)
		private String detail;
	    
	    @NotNull()
	    @Min(value = 1)
	    private double minPrice;
	    
	    private List<PolicyDto> policyDtos = new ArrayList<>();
		
		//private List<CarDto> cars = new ArrayList<>();
}
