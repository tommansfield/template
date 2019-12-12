package com.tom.template.controller;

import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tom.template.config.Properties;
import com.tom.template.exception.AuthRequestException;
import com.tom.template.exception.BadRequestException;
import com.tom.template.exception.InternalServerError;
import com.tom.template.exception.ResourceNotFoundException;
import com.tom.template.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Controller
@ApiIgnore
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

	private final Properties properties;
	private final MessageUtils messages;

	@GetMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
		int status = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String exception = (String) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
		
		if (status >= 400) {
			if (exception != null) log.error(exception);
			if (message != null) log.error(message);
		}
		message = messages.get("error." + String.valueOf(status));
		message = message == null ? messages.get("error.500"): message;
		
		if (isHtmlRequest(request)) {
			model.addAttribute("title", properties.getName());
			model.addAttribute("status", String.valueOf(status));
			model.addAttribute("message", message);
			return "error";
		} else {
			return sendJSONError(status, message);
		}
	}

	private boolean isHtmlRequest(HttpServletRequest request) {
		String acceptHeader = request.getHeader("Accept");
		List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);
		return acceptedMediaTypes.contains(MediaType.TEXT_HTML);
	}

	public String sendJSONError(int status, String message) {
		switch (status) {
		case 400: throw new BadRequestException(message);
		case 401:
		case 403: throw new AuthRequestException(message);
		case 404: throw new ResourceNotFoundException(message);
		default: throw new InternalServerError(message);
		}
	}
	
	@Override
	public String getErrorPath() {
		return "error";
	}
	
}
