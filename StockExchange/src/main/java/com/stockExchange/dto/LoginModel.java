package com.stockExchange.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginModel {

	@NotBlank(message = "please provide your userName")
	private String userName;
	@NotBlank(message = "please provide required password")
	private String password;
	private String role;
}
