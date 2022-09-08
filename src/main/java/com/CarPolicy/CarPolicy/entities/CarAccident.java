package com.CarPolicy.CarPolicy.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="carAccidents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarAccident {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;
	
	@Column(nullable=false)
	public String accidentName;
	
	@Column(nullable=false)
	public String accidentDetail;
	
	public boolean isActive=true;
	
	@Column(nullable=false)
	public int damagePoints;
	
	@ManyToOne
	@JoinColumn(name="car_id",nullable=false)
	private Car car;
}
