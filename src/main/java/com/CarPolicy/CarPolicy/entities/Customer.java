package com.CarPolicy.CarPolicy.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String surname;
	
	@Column(nullable=false, unique = true)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private String mobilePhone;
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	@Column(nullable=false, unique = true)
	private String nationalIdentity;
	
	@OneToOne(cascade=CascadeType.DETACH)
    @JoinColumn(name = "city_id",nullable=false,referencedColumnName = "id")
    private City city;
	
	@OneToMany(mappedBy="customer", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Car> cars = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name="users_roles",
			joinColumns= {@JoinColumn(name="user_id",referencedColumnName="id")},
			inverseJoinColumns= {@JoinColumn(name="role_id", referencedColumnName="id")}
			)
	private List<Role> roles = new ArrayList<>();
	
}
	
	
