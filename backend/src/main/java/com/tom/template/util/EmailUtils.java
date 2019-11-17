package com.tom.template.util;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import com.tom.template.config.Properties;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmailUtils {

	private Properties properties;
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(properties.getMail().getHost());
	    mailSender.setPort(Integer.valueOf(properties.getMail().getPort()));
	    mailSender.setUsername(properties.getMail().getUsername());
	    mailSender.setPassword(properties.getMail().getPassword());
	     
	    java.util.Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	
}
