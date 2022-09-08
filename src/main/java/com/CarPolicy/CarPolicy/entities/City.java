package com.CarPolicy.CarPolicy.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="cities")
@Getter
@Setter
public class City {

	@Id
	private int id;
	private String name;
	private double rate;
	
	@OneToOne(mappedBy = "city")
    private Customer customer;
}
