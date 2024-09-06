package com.stockExchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockExchange.dao.UserService;
import com.stockExchange.dto.LoginModel;
import com.stockExchange.dto.ResponseDto;
import com.stockExchange.dto.UserModel;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	private ResponseEntity<ResponseDto> login(@RequestBody @Valid LoginModel login,Errors errors) {
		//encryption
		if(errors.hasErrors()) {
			StringBuilder validationErrors = new StringBuilder();
			
			for(ObjectError e : errors.getAllErrors()) {
				validationErrors.append(e.getDefaultMessage()).append(",");
			}
			
			ResponseDto response = new ResponseDto(HttpStatus.BAD_REQUEST, "login failed",null,validationErrors.toString());
			return new ResponseEntity<ResponseDto>(response, HttpStatus.valueOf(response.getStatusCode()));
			}
		ResponseDto response = userService.login(login);
		return new ResponseEntity<ResponseDto>(response, HttpStatus.valueOf(response.getStatusCode()));
	}
	
	@PostMapping("/register")
	private ResponseEntity<ResponseDto> register(@RequestBody @Valid UserModel user,Errors errors) {
		
		if(errors.hasErrors()) {
			StringBuilder validationErrors = new StringBuilder();
			
			for(ObjectError e : errors.getAllErrors()) {
				validationErrors.append(e.getDefaultMessage()).append(",");
			}
			
			ResponseDto response = new ResponseDto(HttpStatus.BAD_REQUEST, "Registeration failed",null,validationErrors.toString());
			return new ResponseEntity<ResponseDto>(response, HttpStatus.valueOf(response.getStatusCode()));
			}
		ResponseDto response = userService.register(user);
		return new ResponseEntity<ResponseDto>(response, HttpStatus.valueOf(response.getStatusCode()));
	}
	
//	@PutMapping("/updateProfile")
//	private ResponseEntity<ResponseDto> update(@RequestBody UserProfile user){
//		
//		ResponseDto response = userService.updateUser(user);
//	}
}
