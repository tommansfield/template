package com.tom.template.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tom.template.entity.User;
import com.tom.template.security.CurrentUser;

@RestController
@RequestMapping("api/${app.version}/user")
public class UserController {
	
	@GetMapping("/me")
	public User getCurrentUser(@CurrentUser User user) {
		return user;
	}
	
}
