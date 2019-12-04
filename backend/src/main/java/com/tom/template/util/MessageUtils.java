package com.tom.template.util;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {

	Locale locale;
	
	public String get(String message, Object... args) {
		return messageSource().getMessage(message, args, locale);
	}
	
	@Bean
	public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource  = new ReloadableResourceBundleMessageSource();
	    messageSource.setBasename("classpath:messages");
	    messageSource.setDefaultEncoding("UTF-8");
	    messageSource.setFallbackToSystemLocale(true);
	    return messageSource;
	}
	
}
