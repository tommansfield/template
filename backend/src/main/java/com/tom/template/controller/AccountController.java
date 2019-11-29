package com.tom.template.controller;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tom.template.dto.ApiResponse;
import com.tom.template.dto.ChangeEmail;
import com.tom.template.dto.ChangePassword;
import com.tom.template.entity.User;
import com.tom.template.security.CurrentUser;
import com.tom.template.service.AccountService;
import com.tom.template.util.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
	
	private final ApiResponse response;
	private final AccountService accountService;
	
	@PostMapping("/requestverifyemailtoken")
	public ResponseEntity<?> createVerificationToken(@CurrentUser User user) {
		boolean success = accountService.createToken(user, TokenType.VERIFYEMAIL);
		return success ? response.send(HttpStatus.CREATED, "user.token.sent", user.getEmail())
				: response.send(HttpStatus.BAD_GATEWAY, "error.token.unabletosend", user.getEmail());
	} 
	
	@PostMapping("/requestresetpasswordtoken")
	public ResponseEntity<?> createdResetPasswordToken(@CurrentUser User user) {
		boolean success = accountService.createToken(user, TokenType.RESETPASSWORD);
		return success ? response.send(HttpStatus.CREATED, "user.token.sent", user.getEmail())
				: response.send(HttpStatus.BAD_GATEWAY, "error.token.unabletosend", user.getEmail());
	}
	
	@PostMapping("/verifyemail")
	public ResponseEntity<?> verifyEmail(@CurrentUser User user, @RequestParam(required = true) String token) {
		boolean success = accountService.verifyEmail(user, token);
		return success ? response.send(HttpStatus.OK, "user.email.verified", user.getEmail())
				: response.send(HttpStatus.BAD_GATEWAY, "error.token.incorrecttoken");
	}

	@PostMapping("/resetpassword")
	public ResponseEntity<?> resetPassword(@CurrentUser User user, @RequestParam(required = true) String token) {
		boolean success = accountService.resetPassword(user, token);
		return success ? response.send(HttpStatus.OK, "user.password.reset")
				: response.send(HttpStatus.BAD_REQUEST, "error.token.incorrecttoken");
	}
	
	
	@PostMapping("/changeemail")
	public ResponseEntity<?> changeEmail(@CurrentUser User user, @Valid @RequestBody ChangeEmail changeEmail) {
		log.debug("REST request to change email address: {}", changeEmail.getEmail());
		boolean success = accountService.changeEmail(user, changeEmail);
		return success ? response.send(HttpStatus.OK, "user.email.changed")
				: response.send(HttpStatus.BAD_GATEWAY, "error.token.unabletosend", user.getEmail());
	}
	
	@PostMapping("/changepassword")
	public ResponseEntity<?> changePassword(@CurrentUser User user, @Valid @RequestBody ChangePassword changePass) {
		boolean success = accountService.changePassword(user, changePass);
		return success ? response.send(HttpStatus.OK,"user.password.changed")
				: response.send(HttpStatus.UNAUTHORIZED, "error.auth.wrongpassword");
	}
	
	@PostMapping("/addpassword")
	public ResponseEntity<?> addPassword(@CurrentUser User user, @Valid @RequestBody ChangePassword changePass) {
		boolean success = accountService.changePassword(user, changePass);
		return success ? response.send(HttpStatus.OK,"user.password.added")
				: response.send(HttpStatus.BAD_REQUEST, "error.password.passexists");
	}
	
}
