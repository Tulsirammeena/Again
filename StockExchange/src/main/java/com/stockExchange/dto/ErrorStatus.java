package com.stockExchange.dto;

public enum ErrorStatus {

	SITE_KEY_NOT_FOUND(404, "Site key not found"),
	INVALID_EMAIL_ID(102, "Invalid EmailId"),
	EMPTY_EMAIL(103,"Email ID is empty"),
	EMAIL_ALREADY_AVAILABLE(104, "User already available"),
	USERNAME_EMPTY(105, "User name is empty"),
	PASSWORD_EMPTY(106, "Password is empty"),
	INCORRECT_PASSWORD(107,"Incorrect password"),
	EXCEPTION(500, "Internal Server error");
		
	
	
	
	private final Integer errorCode;
	private final String errorMessage;
 
	private ErrorStatus(Integer errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
 
	public Integer getErrorCode() {
		return errorCode;
	}
 
	public String getErrorMessage() {
		return errorMessage;
	}
}
