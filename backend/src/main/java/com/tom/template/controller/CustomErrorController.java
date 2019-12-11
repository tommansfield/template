package com.tom.template.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tom.template.config.Properties;
import com.tom.template.exception.AuthRequestException;
import com.tom.template.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

	private final Properties properties;
	private final MessageUtils messages;
	
	@GetMapping("/error")
	public String handleError(HttpServletRequest request, Model model) {
	    int status = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    String message;
	    model.addAttribute("title", properties.getName());
	    model.addAttribute("status", String.valueOf(status));
	    if (status == 401) {
	    	message = messages.get("error.401");
	    } else if (status == 404) {
	    	message = messages.get("error.404");
	    } else if (status == 500) {
	    	message = messages.get("error.500");
	    } else {
	    	message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
	    	message = message == null ? messages.get("error.500") : message;
	    }
	    model.addAttribute("message", message);	
	    return "error";
	}

	@Override
	public String getErrorPath() {
		return "error";
	}
	
	@GetMapping("/error/autherror")
	public void accessDenied() {
		throw new AuthRequestException("Access denied.");
	}
	
}
