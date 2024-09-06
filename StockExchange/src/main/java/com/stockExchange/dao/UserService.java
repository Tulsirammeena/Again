package com.stockExchange.dao;

import com.stockExchange.dto.LoginModel;
import com.stockExchange.dto.ResponseDto;
import com.stockExchange.dto.UserModel;
import com.stockExchange.dto.UserProfile;

public interface UserService{

	public ResponseDto login(LoginModel login);
	public ResponseDto register(UserModel user);
	public ResponseDto updateUser(UserProfile user);
}
