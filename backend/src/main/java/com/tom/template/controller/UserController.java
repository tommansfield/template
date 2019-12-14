package com.tom.template.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tom.template.entity.User;
import com.tom.template.security.CurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("${app.baseUrl}/user")
@Api(tags="User services", description="Operations for retrieving user information")
public class UserController {
	
	@GetMapping("/me")
	@ApiOperation(value = "Retrieve current user details")
	public User getCurrentUser(@ApiIgnore @CurrentUser User user) {
		return user;
	}
	
}
