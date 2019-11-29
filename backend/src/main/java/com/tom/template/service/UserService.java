package com.tom.template.service;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tom.template.dto.SignUpRequest;
import com.tom.template.dto.TokenResponse;
import com.tom.template.entity.User;
import com.tom.template.exception.ResourceNotFoundException;
import com.tom.template.repository.UserRepository;
import com.tom.template.security.LocalUser;
import com.tom.template.security.token.TokenProvider;
import com.tom.template.util.MessageUtils;
import com.tom.template.util.validation.ValidEmailValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final MessageUtils messages;
	private final TokenProvider tokenProvider;
	private final UserRepository userRep;
	
	@Autowired @Lazy
	private AuthenticationManager authenticationManager;

	@Autowired @Lazy
	private PasswordEncoder encoder;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		boolean isEmail = ValidEmailValidator.isValid(login);
		Optional<User> user = ValidEmailValidator.isValid(login) ? userRep.findByEmail(login) : userRep.findByUsername(login);
		String error = isEmail ? "error.login.emailnotfound" : "error.login.usernamenotfound";
		if (user.isPresent()) {
			User existingUser = user.get();
			existingUser.setLastLogin(new Date());
			existingUser.getTokens().isEmpty();
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
		signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));
        User user = new User(signUpRequest);
        return userRep.save(user);
	}
	
	public TokenResponse createToken(String login, String password) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(((LocalUser) authentication.getPrincipal()).getId());
        return new TokenResponse(Base64.getEncoder().encodeToString(token.getBytes()));

	}
	
}