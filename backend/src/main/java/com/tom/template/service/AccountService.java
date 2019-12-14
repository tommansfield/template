package com.tom.template.service;

import javax.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tom.template.dto.ChangeEmail;
import com.tom.template.dto.ChangePassword;
import com.tom.template.entity.Role;
import com.tom.template.entity.User;
import com.tom.template.entity.VerificationToken;
import com.tom.template.exception.AuthRequestException;
import com.tom.template.exception.BadRequestException;
import com.tom.template.repository.TokenRepository;
import com.tom.template.repository.UserRepository;
import com.tom.template.util.MessageUtils;
import com.tom.template.util.TokenType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final EmailService email;
	private final MessageUtils messages;
	private final UserRepository userRep;
	private final TokenRepository tokenRep;
	private final PasswordEncoder encoder;

	
	@Transactional
	public void createVerificationToken(User user, TokenType tokenType) {
		VerificationToken token = tokenRep.findByUserAndTokenType(user, tokenType).orElse(new VerificationToken(user, tokenType));
		tokenRep.save(token);
		email.sendMail(user.getEmail(), tokenType);
	}

	@Transactional
	public void resetPassword(User user, String tokenValue) {
		VerificationToken token = tokenRep.findByUserAndTokenType(user, TokenType.RESETPASSWORD)
				.orElseThrow(() -> new AuthRequestException(messages.get("error.token.notoken")));
		if (token.matches(tokenValue)) {
			tokenRep.delete(token);
			user.addRole(Role.VERIFIED_USER);
			user.addRole(Role.RESET_PASSWORD);
			userRep.save(user);
		} else {
			throw new AuthRequestException(messages.get("error.token.incorrecttoken"));
		}
	}
	
	@Transactional
	public void changePassword(User user, ChangePassword changePassword) {
		changePassword.setPassword(encoder.encode(changePassword.getPassword()));
		if (user.getPassword() != null && !user.getPassword().equals(changePassword.getOldPassword())) { 
			throw new AuthRequestException(messages.get("error.auth.wrongpassword"));
		}
		user.setPassword(changePassword.getPassword());
		user.addRole(Role.HAS_PASSWORD);
		userRep.save(user);
	}
	
	@Transactional
	public void verifyEmail(User user, String tokenValue) {
		VerificationToken token = tokenRep.findByUserAndTokenType(user, TokenType.VERIFYEMAIL)
				.orElseThrow(() -> new BadRequestException(messages.get("error.token.alreadyverified", user.getEmail())));
		if (token.matches(tokenValue)) {
			tokenRep.delete(token);
		} else {
			throw new AuthRequestException(messages.get("error.token.incorrecttoken"));
		}
	}
	
	@Transactional
	public void changeEmail(User user, ChangeEmail changeEmail) {
		user.setEmail(changeEmail.getEmail());
		user.removeRole(Role.VERIFIED_USER);
		createVerificationToken(user, TokenType.VERIFYEMAIL);
	}
	
}
