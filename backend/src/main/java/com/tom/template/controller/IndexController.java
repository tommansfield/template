package com.tom.template.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tom.template.config.Properties;
import com.tom.template.util.MessageUtils;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class IndexController {
	
	
	private Properties properties;
	
	@Autowired
	private MessageUtils messages;

	@GetMapping("/")
    public String welcome(Model model) {
		String title = messages.getMessage("home.title", properties.getName());
        model.addAttribute("title", title);
        model.addAttribute("swaggerUrl", properties.getSwaggerUrl());
        return "index";
	}
	
}
