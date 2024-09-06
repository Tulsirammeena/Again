package com.stockExchange.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserProfile {
	
	private String fullName;
	private String city;
	private String phoneNumber;
	private String emailAddress;
}
