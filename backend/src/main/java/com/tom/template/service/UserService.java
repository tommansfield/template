package com.tom.template.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tom.template.dto.ChangeEmail;
import com.tom.template.dto.ChangePassword;
import com.tom.template.dto.SignUpRequest;
import com.tom.template.entity.Role;
import com.tom.template.entity.User;
import com.tom.template.entity.VerificationToken;
import com.tom.template.exception.BadRequestException;
import com.tom.template.exception.ResourceNotFoundException;
import com.tom.template.repository.UserRepository;
import com.tom.template.security.LocalUser;
import com.tom.template.util.MessageUtils;
import com.tom.template.util.TokenType;
import com.tom.template.util.validation.ValidEmailValidator;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

	private UserRepository userRep;
	private EmailService emailService;
	private MessageUtils messages;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		boolean isEmail = ValidEmailValidator.isValid(login);
		Optional<User> user = ValidEmailValidator.isValid(login) ? userRep.findByEmail(login) : userRep.findByUsername(login);
		String error = isEmail ? "error.login.emailnotfound" : "error.login.usernamenotfound";
		if (user.isPresent()) {
			User existingUser = user.get();
			existingUser.setLastLogin(new Date());
			return LocalUser.create(existingUser);
		} else {
			throw new UsernameNotFoundException(messages.getMessage(error, login));
		}
	}

	@Transactional
    public User loadUserById(Long id) {
        User user = userRep.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException(messages.getMessage("error.resource.notfound", "user", "id", id)));
        return user;
	}

	@Transactional
	public User createUser(@Valid SignUpRequest signUpRequest) {
        User user = new User(signUpRequest);
        return userRep.save(user);
	}
	
	
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
			emailService.sendMail(user.getEmail(), tokenType);
		} catch (Exception e) {
			user.removeToken(token);
			System.out.println(e);
			return false;
		}
		userRep.save(user);
		return true;
	}
	
	@Transactional
	public boolean verifyEmail(User user, String tokenValue) {
		if (!user.hasToken(TokenType.VERIFYEMAIL)) {
			throw new BadRequestException(messages.getMessage("error.token.alreadyverified", user.getEmail()));
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
		if (user.getPassword() != null && !user.getPassword().equals(changePassword.getOldPassword())) {
			return false;
		}
		user.setPassword(changePassword.getPassword());
		userRep.save(user);
		return true;
	}
	
	@Transactional
	public boolean addPassword(User user, ChangePassword changePassword) {
		if (user.getPassword() != null) {
			return false;
		}
		user.setPassword(changePassword.getPassword());
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