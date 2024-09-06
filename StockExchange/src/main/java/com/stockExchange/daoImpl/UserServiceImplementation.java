package com.stockExchange.daoImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stockExchange.dao.UserService;
import com.stockExchange.dto.LoginModel;
import com.stockExchange.dto.ResponseDto;
import com.stockExchange.dto.UserModel;
import com.stockExchange.dto.UserProfile;
import com.stockExchange.dto.UserResponseModel;
import com.stockExchange.entity.UserEntity;
import com.stockExchange.repository.UserRepo;
import com.stockExchange.utils.CommonUtils;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public ResponseDto login(LoginModel login) {

		Optional<UserEntity> existingUser = userRepo.findByUserName(login.getUserName());
		if (existingUser.isPresent()) {
			UserEntity confirmUser = userRepo.getByUserName(login.getUserName());
			if(!confirmUser.getPassword().equals(login.getPassword()))
					return new ResponseDto(HttpStatus.BAD_REQUEST, "Login Failed",null,"Invalid password");
			
			if (confirmUser.getPassword().equals(login.getPassword())
					&& confirmUser.getRole().equals(login.getRole())) {
				UserResponseModel userData = new UserResponseModel(confirmUser.getUserName(), confirmUser.getRole());
				return new ResponseDto(HttpStatus.OK, "Login successful", userData);
			}
		}

		return new ResponseDto(HttpStatus.BAD_REQUEST, "Login Failed",null,"Invalid Username");
	}

	@Override
	public ResponseDto register(UserModel user) {
		
		if(isUserNameExist(user.getUserName()))
			return new ResponseDto(HttpStatus.CONFLICT, "Registeration failed", null, "Username already exists");
		
		if(isPanExist(user.getPanNo()))
			return new ResponseDto(HttpStatus.CONFLICT, "Registeration failed", null, "Given pan already exists");
		
		UserEntity newUser = new UserEntity(user);
//		newUser.setDAN(CommonUtils.generateDAN());
		userRepo.save(newUser);
		return new ResponseDto(HttpStatus.OK, "Registration successful", newUser);
	}

	@Override
	public ResponseDto updateUser(UserProfile user) {

		UserEntity dbUser = userRepo.getByEmailAddress(user.getEmailAddress());

		dbUser.setCity(user.getCity());
		dbUser.setFullName(user.getFullName());
		dbUser.setEmailAddress(user.getEmailAddress());
		dbUser.setPhoneNumber(user.getPhoneNumber());

		userRepo.save(dbUser);

		return new ResponseDto(HttpStatus.ACCEPTED, "Updation successful", user);
	}

	public boolean isUserNameExist(String userName) {

		Optional<UserEntity> existingUser = userRepo.findByUserName(userName);
		if (existingUser.isPresent())
			return true;
		return false;
	}

	public boolean isPanExist(String panNo) {

		Optional<UserEntity> existingUser = userRepo.findByPanNo(panNo);
		if (existingUser.isPresent())
			return true;
		return false;
	}

}
