package com.tom.template.security;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class AuthEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		if (isHtmlRequest(request)) {
			request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 401);
			request.getRequestDispatcher("/error").forward(request, response);
		} else {
			response.sendRedirect(("/error/autherror"));
		}
	}

	private boolean isHtmlRequest(HttpServletRequest request) {
		String acceptHeader = request.getHeader("Accept");
		List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);
		return acceptedMediaTypes.contains(MediaType.TEXT_HTML);
	}
}
