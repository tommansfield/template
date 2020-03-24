package com.tom.template.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Controller;
import com.tom.template.util.MessageUtils;

@Controller
public class AuthEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	private MessageUtils messages;

	@SneakyThrows
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
		System.out.println("hi");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, messages.get("error.auth.wrongpassword"));
	}
	
}
