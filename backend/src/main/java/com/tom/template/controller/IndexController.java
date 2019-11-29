package com.tom.template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.tom.template.config.Properties;
import com.tom.template.util.MessageUtils;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	
	private final Properties properties;
	private final MessageUtils messages;

	@GetMapping("/")
    public String welcome(Model model) {
		String title = messages.get("home.title", properties.getName());
        model.addAttribute("title", title);
        model.addAttribute("swaggerUrl", properties.getSwaggerUrl());
        return "index";
	}
	
}
