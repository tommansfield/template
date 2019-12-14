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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.baseUrl}/account")
@Api(tags="Account Services", description="Operations for user account maintenance")
public class AccountController {
	
	private final AccountService accountService;
	private final MessageUtils messages;
	
	@ApiOperation(value = "Request an email verification token")
	@PostMapping("/requestverifyemailtoken")
	public ResponseEntity<ApiResponse> createVerificationToken(@ApiIgnore @CurrentUser User user) {
		accountService.createVerificationToken(user, TokenType.VERIFYEMAIL);
		return new ApiResponse(HttpStatus.CREATED, messages.get("user.token.sent")).send();
	} 
	
	@ApiOperation(value = "Request a password reset token")
	@PostMapping("/requestresetpasswordtoken")
	public ResponseEntity<ApiResponse> createdResetPasswordToken(@ApiIgnore @CurrentUser User user) {
		accountService.createVerificationToken(user, TokenType.RESETPASSWORD);
		return new ApiResponse(HttpStatus.CREATED, messages.get("user.token.sent")).send();
	}
	
	@ApiOperation(value = "Add a password for an externally created account")
	@PostMapping("/addpassword")
	public ResponseEntity<ApiResponse> addPassword(@ApiIgnore @CurrentUser User user, @Valid @RequestBody ChangePassword changePass) {
		accountService.changePassword(user, changePass);
		return new ApiResponse(HttpStatus.OK, messages.get("user.password.added")).send();
	}
	
	@ApiOperation(value = "Reset a forgotten password")
	@PostMapping("/resetpassword")
	public ResponseEntity<ApiResponse> resetPassword(@ApiIgnore @CurrentUser User user, @RequestParam(required = true) String token) {
		accountService.resetPassword(user, token);
		return new ApiResponse(HttpStatus.OK, messages.get("user.password.reset")).send();
	}
	
	@ApiOperation(value = "Modify a password")
	@PostMapping("/changepassword")
	public ResponseEntity<ApiResponse> changePassword(@ApiIgnore @CurrentUser User user, @Valid @RequestBody ChangePassword changePass) {
		accountService.changePassword(user, changePass);
		return new ApiResponse(HttpStatus.OK, messages.get("user.password.changed")).send();
	}
	
	@ApiOperation(value = "Verify an email address")
	@PostMapping("/verifyemail")
	public ResponseEntity<ApiResponse> verifyEmail(@ApiIgnore @CurrentUser User user, @RequestParam(required = true) String token) {
		accountService.verifyEmail(user, token);
		return new ApiResponse(HttpStatus.OK, messages.get("user.email.verified")).send();
	}
	
	@ApiOperation(value = "Modify an email address")
	@PostMapping("/changeemail")
	public ResponseEntity<ApiResponse> changeEmail(@ApiIgnore @CurrentUser User user, @Valid @RequestBody ChangeEmail changeEmail) {
		accountService.changeEmail(user, changeEmail);
		return new ApiResponse(HttpStatus.OK, messages.get("user.email.changed")).send();
	}
	
}
