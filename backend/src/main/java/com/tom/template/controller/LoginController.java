package com.tom.template.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tom.template.dto.LoginRequest;
import com.tom.template.dto.SignUpRequest;
import com.tom.template.dto.TokenResponse;
import com.tom.template.entity.User;
import com.tom.template.security.token.TokenProvider;
import com.tom.template.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;
 
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags="Login Services", description="Operations for account creation and user login")
public class LoginController {

	private final UserService userService;
	private final TokenProvider tokenProvider;

	@PostMapping("/login")
	@ApiOperation(value = "Retrieve an access token")
	public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		log.debug("Login attempt for local user: {}", loginRequest.getEmail());
		TokenResponse token = tokenProvider.createToken(loginRequest.getEmail(), loginRequest.getPassword());
		return ResponseEntity.ok(token);
	}

	@PostMapping("/signup")
	@ApiOperation(value = "Register a new account")
	public ResponseEntity<TokenResponse> registerUser(@Valid @RequestBody SignUpRequest signup) throws InterruptedException {
		User user = userService.createUser(signup);
		log.debug("Registering new local user account for: {}", user.getEmail());
		TokenResponse token = tokenProvider.createToken(user.getId());
		return ResponseEntity.ok(token);
	}

	@ApiIgnore
	@GetMapping("/callback")
	private ResponseEntity<TokenResponse> oAuth2TokenCallback(HttpServletRequest request, HttpServletResponse response) {
		TokenResponse token = userService.processToken(response);
		return ResponseEntity.ok(token);
	}

}
