package com.tom.template.service;

import java.util.HashSet;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tom.template.dto.ChangeEmail;
import com.tom.template.dto.ChangePassword;
import com.tom.template.entity.Role;
import com.tom.template.entity.User;
import com.tom.template.entity.VerificationToken;
import com.tom.template.exception.BadRequestException;
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
	private final PasswordEncoder encoder;

	@Transactional
	public boolean createToken(User user, TokenType tokenType) {
		Set<VerificationToken> tokenCopies = new HashSet<>(user.getTokens());
		for (VerificationToken token : tokenCopies) {
			if (token.getTokenType().equals(tokenType)) {
				user.removeToken(token);
			}
		}
		VerificationToken token = new VerificationToken(user, tokenType);
		user.addToken(token);
		try {
			email.sendMail(user.getEmail(), tokenType);
		} catch (Exception e) {
			user.removeToken(token);
			return false;
		}
		userRep.save(user);
		return true;
	}

	@Transactional
	public boolean verifyEmail(User user, String tokenValue) {
		if (!user.hasToken(TokenType.VERIFYEMAIL)) {
			throw new BadRequestException(messages.get("error.token.alreadyverified", user.getEmail()));
		}
		Set<VerificationToken> tokenCopies = new HashSet<>(user.getTokens());
		for (VerificationToken t : tokenCopies) {
			if (t.getToken().equals(tokenValue) && t.getTokenType().equals(TokenType.VERIFYEMAIL)) {
				user.removeToken(t);
				user.addRole(Role.VERIFIED_USER);
				userRep.save(user);
				return true;
			}
		}
		return false;
	}

	@Transactional
	public boolean changeEmail(User user, ChangeEmail changeEmail) {
		user.setEmail(changeEmail.getEmail());
		user.removeRole(Role.VERIFIED_USER);
		return createToken(user, TokenType.VERIFYEMAIL);
	}

	@Transactional
	public boolean changePassword(User user, ChangePassword changePassword) {
		changePassword.setPassword(encoder.encode(changePassword.getPassword()));
		if (user.getPassword() != null && !user.getPassword().equals(changePassword.getOldPassword())) { return false; }
		user.setPassword(changePassword.getPassword());
		userRep.save(user);
		return true;
	}

	@Transactional
	public boolean addPassword(User user, ChangePassword changePassword) {
		if (user.getPassword() != null) { return false; }
		user.setPassword(encoder.encode(changePassword.getPassword()));
		userRep.save(user);
		return true;
	}

	@Transactional
	public boolean resetPassword(User user, String tokenValue) {
		Set<VerificationToken> tokenCopies = new HashSet<>(user.getTokens());
		for (VerificationToken t : tokenCopies) {
			if (t.getToken().equals(tokenValue) && t.getTokenType().equals(TokenType.RESETPASSWORD)) {
				user.removeToken(t);
				user.addRole(Role.VERIFIED_USER);
				user.addRole(Role.RESET_PASSWORD);
				userRep.save(user);
				return true;
			}
		}
		return false;
	}
}
