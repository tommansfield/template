package com.tom.template.controller;

import java.io.IOException;
import java.util.Base64;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tom.template.dto.LoginRequest;
import com.tom.template.dto.SignUpRequest;
import com.tom.template.dto.TokenResponse;
import com.tom.template.entity.User;
import com.tom.template.exception.AuthRequestException;
import com.tom.template.exception.BadRequestException;
import com.tom.template.security.LocalUser;
import com.tom.template.security.token.TokenProvider;
import com.tom.template.service.UserService;
import com.tom.template.util.CookieUtils;
import com.tom.template.util.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class LoginController {
	
	private MessageUtils messages;
	private UserService userService;
	private TokenProvider tokenProvider;
	private PasswordEncoder encoder;
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		String login = loginRequest.getEmail() == null ? loginRequest.getUsername() : loginRequest.getEmail();
		log.debug("Login attempt for local user: {}", login);
		String token = createToken(login, loginRequest.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }
	
	@PostMapping("/signup")
    public ResponseEntity<TokenResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		signUpRequest = userService.decode(signUpRequest);
		signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));
		User user = userService.createUser(signUpRequest);
        log.debug("Registering new local user account for: {}", user.getEmail());
        String token = createToken(user.getEmail(), signUpRequest.getMatchingPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }
	
	@GetMapping("/callback")
	private ResponseEntity<TokenResponse> oAuth2TokenCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = CookieUtils.getCookie(request, "token").map(Cookie::getValue).orElse(null);
		if (token == null) {
			throw new AuthRequestException(messages.getMessage("error.oauth.authrefused"));
		}
		String decodedToken =  new String(Base64.getDecoder().decode(token));
		CookieUtils.addCookie(response, "token", null, 0);
		return ResponseEntity.ok(new TokenResponse(decodedToken));
	}
	
	@GetMapping("/callbackerror")
	private ResponseEntity<?> oAuth2CallbackError(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.addCookie(response, "token", null, 1);
		throw new BadRequestException(messages.getMessage("error.oauth.authrefused"));
	}
	
	public String createToken(String login, String password) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(((LocalUser) authentication.getPrincipal()).getId());
        return token;
	}
	
}
