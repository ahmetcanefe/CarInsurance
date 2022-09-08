package com.CarPolicy.CarPolicy.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.CarPolicy.CarPolicy.entities.Customer;
import com.CarPolicy.CarPolicy.repositories.CustomerRepository;



@Service
public class CustomUserDetailsService implements UserDetailsService{

	private CustomerRepository customerRepository;
	
	public CustomUserDetailsService(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException 
	{
		Customer user = customerRepository.findByEmail(email);
		if(user != null)
		{
			org.springframework.security.core.userdetails.User authenticatedUser = new org.springframework.security.core.userdetails.User(				
					  user.getEmail(),
					  user.getPassword(),
					  user.getRoles().stream()
					              .map((role) -> new SimpleGrantedAuthority(role.getName()))
					              .collect(Collectors.toList())
					);
			return authenticatedUser;
		}else {
			throw new UsernameNotFoundException("Invalid username and password");
		}		
	}

	
}
