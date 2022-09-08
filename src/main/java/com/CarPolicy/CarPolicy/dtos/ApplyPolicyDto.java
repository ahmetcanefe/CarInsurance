package com.CarPolicy.CarPolicy.dtos;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyPolicyDto {

	@NotEmpty()
	private String licensePlate;
	
	@NotEmpty()
	private String nationalIdentity;
	
	@DateTimeFormat(pattern = "yyy-MM-dd")
	private Date startDate;
	
    @DateTimeFormat(pattern = "yyy-MM-dd")
	private Date endDate;
    
    private double price;
    
    private int policyId;
}
