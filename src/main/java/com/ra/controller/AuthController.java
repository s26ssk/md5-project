package com.ra.controller;

import com.ra.dto.request.UserLogin;
import com.ra.dto.request.UserRegister;
import com.ra.dto.response.JwtResponse;
import com.ra.service.IUserService;
import com.ra.util.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private IUserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> handleLogin(@RequestBody UserLogin userLogin) throws AppException {
		return new ResponseEntity<>(userService.login(userLogin), HttpStatus.OK);
	}
	@PostMapping("/logout")
	public ResponseEntity<String> handleLogout() {
		SecurityContextHolder.clearContext();
		return new ResponseEntity<>("Logout successful", HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> handleRegister(@RequestBody UserRegister userRegister) throws AppException {
		userService.register(userRegister);
		return new ResponseEntity<>("Register successful",HttpStatus.CREATED);
	}
	
}
