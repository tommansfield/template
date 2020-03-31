package com.tom.template.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.template.dto.LoginRequest;
import com.tom.template.dto.SignUpRequest;
import com.tom.template.dto.TokenResponse;
import com.tom.template.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
 
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags="Login Services")
public class LoginController {

	private final UserService userService;
	
	@PostMapping("/login")
	@ApiOperation(value = "Retrieve an access token")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		TokenResponse token = userService.login(loginRequest);
		return ResponseEntity.ok(token);
	}

	@PostMapping("/signup")
	@ApiOperation(value = "Register a new account")
	public ResponseEntity<TokenResponse> registerUser(@Valid @RequestBody SignUpRequest signup) {
		TokenResponse token = userService.createUser(signup);
		return ResponseEntity.ok(token);
	}

}
