package com.CarPolicy.CarPolicy.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDto {
	
	    private int id;	
	   
	    private double price;
		
		private Date startDate;
		
		private Date endDate; 
	    
		private CarDto car;
		
		private PolicyTypeDto policyType;
}
