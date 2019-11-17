package com.tom.template.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.tom.template.config.Properties;
import com.tom.template.util.MessageUtils;
import com.tom.template.util.TokenType;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

	public Properties properties;;
	public MessageUtils messages;
	public JavaMailSender emailSender;

	public void sendMail(String email, TokenType tokenType) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("thomas.mansfield@capgemini.com");
		message.setSubject("hi");
		message.setText("test");
		emailSender.send(message);
	}
	
}
