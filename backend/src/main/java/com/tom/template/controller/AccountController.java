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
import com.tom.template.util.MessageUtils;
import com.tom.template.util.TokenType;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.version}/account")
public class AccountController {
	
	private final AccountService accountService;
	private final MessageUtils messages;
	
	@PostMapping("/requestverifyemailtoken")
	public ResponseEntity<ApiResponse> createVerificationToken(@CurrentUser User user) {
		accountService.createToken(user, TokenType.VERIFYEMAIL);
		return new ApiResponse(HttpStatus.CREATED, messages.get("user.token.sent")).send();
	} 
	
	@PostMapping("/requestresetpasswordtoken")
	public ResponseEntity<ApiResponse> createdResetPasswordToken(@CurrentUser User user) {
		accountService.createToken(user, TokenType.RESETPASSWORD);
		return new ApiResponse(HttpStatus.CREATED, messages.get("user.token.sent")).send();
	}
	
	@PostMapping("/addpassword")
	public ResponseEntity<ApiResponse> addPassword(@CurrentUser User user, @Valid @RequestBody ChangePassword changePass) {
		accountService.changePassword(user, changePass);
		return new ApiResponse(HttpStatus.OK, messages.get("user.password.added")).send();
	}
	
	@PostMapping("/resetpassword")
	public ResponseEntity<ApiResponse> resetPassword(@CurrentUser User user, @RequestParam(required = true) String token) {
		accountService.resetPassword(user, token);
		return new ApiResponse(HttpStatus.OK, messages.get("user.password.reset")).send();
	}
	
	@PostMapping("/changepassword")
	public ResponseEntity<ApiResponse> changePassword(@CurrentUser User user, @Valid @RequestBody ChangePassword changePass) {
		accountService.changePassword(user, changePass);
		return new ApiResponse(HttpStatus.OK, messages.get("user.password.changed")).send();
	}
	
	@PostMapping("/verifyemail")
	public ResponseEntity<ApiResponse> verifyEmail(@CurrentUser User user, @RequestParam(required = true) String token) {
		accountService.verifyEmail(user, token);
		return new ApiResponse(HttpStatus.OK, messages.get("user.email.verified")).send();
	}
	
	@PostMapping("/changeemail")
	public ResponseEntity<ApiResponse> changeEmail(@CurrentUser User user, @Valid @RequestBody ChangeEmail changeEmail) {
		accountService.changeEmail(user, changeEmail);
		return new ApiResponse(HttpStatus.OK, messages.get("user.email.changed")).send();
	}
	
	
	
}
