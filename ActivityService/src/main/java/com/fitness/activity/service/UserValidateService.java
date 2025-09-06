package com.fitness.activity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class UserValidateService {

	@Autowired
	private WebClient userServiceWebClient;

	public Boolean validateUser(String userId) {
		try {

			return userServiceWebClient.get().uri("/api/user/{userId}/validate", userId).retrieve()
					.bodyToMono(Boolean.class).block();

		} catch (WebClientResponseException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND)
				throw new RuntimeException("User not found " + userId);
			else if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
				throw new RuntimeException("Invalid Request" + userId);
		}
		return false;
	}

}
