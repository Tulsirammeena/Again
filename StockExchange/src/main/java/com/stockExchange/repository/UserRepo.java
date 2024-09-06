package com.stockExchange.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockExchange.entity.HoldingEntity;
import com.stockExchange.entity.UserEntity;
@Repository																												
public interface UserRepo extends JpaRepository<UserEntity, String>{

//	public Optional<UserEntity> findByUsernameAndPasswordAndRole(String userName, String password, String role);

	public Optional<UserEntity> findByEmailAddress(String emailAddress);

	public UserEntity getByEmailAddress(String emailAddress);

	public Optional<UserEntity> findByUserName(String userName);

	public Optional<UserEntity> findByPanNo(String panNo);

	public UserEntity getByUserName(String userName);

//	public Optional<Set<HoldingEntity>> findHoldingsByUser_UserName(String userName);

//	public Optional<Set<HoldingEntity>> findHoldingsByUserName(String userName);


}
