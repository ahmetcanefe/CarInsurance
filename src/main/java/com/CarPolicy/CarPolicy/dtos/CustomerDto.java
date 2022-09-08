package com.CarPolicy.CarPolicy.dtos;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.CarPolicy.CarPolicy.entities.City;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private int id;
	
    @NotEmpty()
    @Size(min=3, max=20)
	private String name;
	
    @NotEmpty()
	private String surname;
	
    @NotEmpty()
	private String mobilePhone;
	
    @NotEmpty()
	private String email;
	
    @NotEmpty()
	private String password;
	
    @JsonFormat(pattern="yyyy-MM-dd")
	private Date birthDate;
	
    @NotEmpty()
	private String nationalIdentity;
    
    private City city;
    
    private int cityId;
    
	List<CarDto> cars;
	
	int[] roleIds;
}
