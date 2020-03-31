package com.tom.template.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tom.template.exception.EmailException;
import com.tom.template.util.MessageUtils;
import com.tom.template.util.TokenType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	public final MessageUtils messages;
	public final JavaMailSender emailSender;

	void sendMail(String email, TokenType tokenType) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(email);
			message.setSubject("hi");
			message.setText("test");
			emailSender.send(message);
		} catch (Exception ex) {
			throw new EmailException(messages.get("error.token.unabletosend", email));
		}
	}
	
}
