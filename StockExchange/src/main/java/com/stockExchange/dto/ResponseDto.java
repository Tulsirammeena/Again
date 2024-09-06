package com.stockExchange.dto;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ResponseDto {

	private int statusCode;
	private String message;
	private Object data;
	private String errorMessage;
	
	
	public ResponseDto(HttpStatus statusCode, String message) {
		this.statusCode = statusCode.value();
		this.message = message;
	}


	public ResponseDto(HttpStatus statusCode, String message, Object data) {
		this.statusCode = statusCode.value();
		this.message = message;
		this.data = data;
	}
	
	public ResponseDto(HttpStatus statusCode, String message,Object data, String errorMessage) {
		this.statusCode = statusCode.value();
		this.message = message;
		this.data = data;
		this.errorMessage = errorMessage;
	}
}
