package com.stockExchange.entity;


import java.time.LocalDateTime;
import java.util.Set;

import com.stockExchange.dto.UserModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_info")
public class UserEntity {

	
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long userId;
	@Id
	private String userName;
	private String fullName;
	private String city;
	private String phoneNumber;
	private String password;
	private String emailAddress;
	private String role;
	private String panNo;
	private LocalDateTime createdAt;
//	private Long DAN;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY,cascade = CascadeType.PERSIST,
			targetEntity = HoldingEntity.class)
	private Set<HoldingEntity> holdings;
	
	public UserEntity(UserModel user) {
		this.userName = user.getUserName();
		this.password = user.getPassword();
		this.fullName = user.getFullName();
		this.city=user.getCity();
		this.emailAddress = user.getEmailAddress();
		this.phoneNumber = user.getPhoneNumber();
		this.role = user.getRole();
		this.panNo = user.getPanNo();
		this.createdAt = LocalDateTime.now();
	}
}
