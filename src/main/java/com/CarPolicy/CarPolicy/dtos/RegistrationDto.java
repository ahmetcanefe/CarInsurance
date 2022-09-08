package com.CarPolicy.CarPolicy.dtos;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.CarPolicy.CarPolicy.entities.City;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    private int id;
	
    @NotEmpty()
    @Size(min=3, max=15)
	private String name;
	
    @NotEmpty()
    @Size(min=3, max=25)
	private String surname;
	
    @NotEmpty()
    @Size(min=11, max=11)
	private String mobilePhone;
	
    @NotEmpty()
    @Size()
	private String email;
	
    @NotEmpty()
    @Size(min=6, max=15)
	private String password;
    
    private City city;
    
    private int cityId;
	
	@NotNull()
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;
	
    @NotEmpty()
    @Size(min=11, max=11)
	private String nationalIdentity;
}
