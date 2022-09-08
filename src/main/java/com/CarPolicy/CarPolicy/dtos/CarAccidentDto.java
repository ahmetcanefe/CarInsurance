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
public class CarAccidentDto {

    public int id;
	
    @NotEmpty()
    @Size(min=3, max=15)
	public String accidentName;
	
    @NotEmpty()
    @Size(min=3, max=50)
	public String accidentDetail;
	
    @NotNull()
    @Min(value = 1)
    @Max(value = 10)
	public int damagePoints;
    
    public CarDto car;
    
    public boolean isActive;
    
    public int carId;

}
