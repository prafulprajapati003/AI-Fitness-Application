package com.fitness.apigateway.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;
	
	private String keycloakId;

	
	@NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have atleast 6 character")
	private String password;
	private String firstName;
	private String lastName;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getKeycloakId() {
		return keycloakId;
	}
	public void setKeycloakId(String keycloakId) {
		this.keycloakId = keycloakId;
	}

	
}
