package com.CarPolicy.CarPolicy.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCarAccidentDto {
	 public int id;
		
	    @NotEmpty()
	    @Size(min=3, max=15)
		public String accidentName;
		
	    @NotEmpty()
	    @Size(min=3, max=50)
		public String accidentDetail;
		

	    
	    public boolean isActive;
	    
	    public int carId;
}
