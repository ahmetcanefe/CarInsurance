package com.CarPolicy.CarPolicy.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="policyTypes")
@Getter
@Setter
public class PolicyType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String detail;
	
	@Column(nullable=false)
	private double minPrice;
	
	@OneToMany(mappedBy="policyType",fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Policy> policies = new ArrayList<>();
}
