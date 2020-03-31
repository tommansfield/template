package com.tom.template.controller;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@RequestMapping("/error")
	public String getError(@RequestParam String error, HttpServletRequest request, Model model) {
		return handleError(request, model);
	}
	
	@GetMapping("/internalerror")
	public String handleError(HttpServletRequest request, Model model) {
		int status = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
		if (status >= 400 && message != null) {
			log.error(message);
		}
		if (isHtmlRequest(request)) {
			message = messages.get("error." + status);
			message = message == null ? messages.get("error.500"): message;
			model.addAttribute("title", properties.getName());
			model.addAttribute("status", String.valueOf(status));
			model.addAttribute("message", message);
			model.addAttribute("frontEndUri", properties.getFrontEndUri());
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

	private String sendJSONError(int status, String message) {
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
