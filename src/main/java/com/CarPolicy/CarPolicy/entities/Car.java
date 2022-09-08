package com.CarPolicy.CarPolicy.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable=false)
	private String licensePlate;
	
	@Column(nullable=false)
	private int manufacturingYear;
	
	@Column(nullable=false)
	private float mileage;
	
	@Column(nullable=false)
	private String color;
	
    private boolean isActive=true;
	
    @OneToMany(mappedBy="car", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CarAccident> carAccidents = new ArrayList<>();
    
    @OneToMany(mappedBy="car", fetch = FetchType.LAZY)
	private List<Policy> policies = new ArrayList<>();
    
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@OneToOne(cascade=CascadeType.DETACH)
    @JoinColumn(name = "model_id",referencedColumnName = "id")
    private Model model;

}
