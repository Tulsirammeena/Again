package com.stockExchange.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
	@NotBlank(message = "Please provide a username")	
	private String userName;
	@NotBlank(message = "Please provide your name")
	private String fullName;
	@NotBlank(message = "Please provide your city")
	private String city;
	@NotBlank(message = "Please provide your contact number")
	private String phoneNumber;
	@NotBlank(message = "Please provide your password")
	private String password;
	@NotBlank(message = "Please provide your email Address")
	private String emailAddress;
	private String role;
	@NotBlank(message = "Please provide your PAN number")
	private String panNo;
}
