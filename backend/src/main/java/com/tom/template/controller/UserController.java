package com.tom.template.controller;

import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tom.template.entity.User;
import com.tom.template.entity.VerificationToken;
import com.tom.template.repository.UserRepository;
import com.tom.template.security.CurrentUser;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	private final UserRepository userRep;
	
	@GetMapping("/me")
	public User getCurrentUser(@CurrentUser User user) {
		return user;
	}
	
	@GetMapping("/tokens")
	public Set<VerificationToken> tokens(@CurrentUser User user) {
		return user.getTokens();
	}
	
}
