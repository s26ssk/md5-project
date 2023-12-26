package com.ra.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserLogin {
	@NotEmpty(message = "Username cannot be empty")
	private String username;
	@NotEmpty(message = "Password cannot be empty")
	private String password;
}
