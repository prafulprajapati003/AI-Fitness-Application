package com.fitness.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepo userRepo;

	public UserResponse register(RegisterRequest request) {

		if (userRepo.existsByEmail(request.getEmail())) {
			User existingUser = userRepo.findByEmail(request.getEmail());

			UserResponse userResponse = new UserResponse();

			userResponse.setId(existingUser.getId());
			userResponse.setPassword(existingUser.getPassword());
			userResponse.setEmail(existingUser.getEmail());
			userResponse.setKeycloakId(existingUser.getKeycloakId());
			userResponse.setFirstName(existingUser.getFirstName());
			userResponse.setLastName(existingUser.getLastName());
			userResponse.setCreatedAt(existingUser.getCreatedAt());
			userResponse.setUpdatedAt(existingUser.getUpdatedAt());

			return userResponse;

			// throw new RuntimeException("Email already exist");
		}

		User user = new User();

		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setKeycloakId(request.getKeycloakId());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());

		User savedUser = userRepo.save(user);

		UserResponse userResponse = new UserResponse();

		userResponse.setId(savedUser.getId());
		userResponse.setPassword(savedUser.getPassword());
		userResponse.setEmail(savedUser.getEmail());
		userResponse.setKeycloakId(savedUser.getKeycloakId());
		userResponse.setFirstName(savedUser.getFirstName());
		userResponse.setLastName(savedUser.getLastName());
		userResponse.setCreatedAt(savedUser.getCreatedAt());
		userResponse.setUpdatedAt(savedUser.getUpdatedAt());

		return userResponse;
	}

	public UserResponse getUserProfile(String userId) {

		User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User Not found"));

		UserResponse userResponse = new UserResponse();

		userResponse.setId(user.getId());
		userResponse.setPassword(user.getPassword());
		userResponse.setEmail(user.getEmail());
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setCreatedAt(user.getCreatedAt());
		userResponse.setUpdatedAt(user.getUpdatedAt());

		return userResponse;
	}

	public Boolean existByUserId(String userId) {

		/* ("Calling User Validation API for userId: {}", userId); */
		return userRepo.existsByKeycloakId(userId);
	}

}
