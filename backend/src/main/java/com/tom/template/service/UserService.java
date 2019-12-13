package com.tom.template.service;

import java.util.Base64;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tom.template.dto.LoginRequest;
import com.tom.template.dto.SignUpRequest;
import com.tom.template.dto.TokenResponse;
import com.tom.template.entity.User;
import com.tom.template.exception.AuthRequestException;
import com.tom.template.exception.ResourceNotFoundException;
import com.tom.template.repository.UserRepository;
import com.tom.template.security.LocalUser;
import com.tom.template.security.token.TokenProvider;
import com.tom.template.util.MessageUtils;
import com.tom.template.util.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final MessageUtils messages;
	private final TokenProvider tokenProvider;
	private final UserRepository userRep;
	
	@Lazy
	@Autowired
	private AccountService accountService;
	
	@Lazy
	@Autowired 
	private PasswordEncoder encoder;
	
	@Lazy
	@Autowired 
	private AuthenticationManager authenticationManager;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = userRep.findByEmail(login).orElseThrow(() 
				-> new UsernameNotFoundException(messages.get("error.login.emailnotfound", login)));
			return LocalUser.create(user);
	}

	public TokenResponse login(LoginRequest loginRequest) {
		log.debug("Login attempt for local user: {}", loginRequest.getEmail());
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        return tokenProvider.createAccessToken(((LocalUser) authentication.getPrincipal()).getId());
	}
	
	@Transactional
    public User loadUserById(Long id) {
        User user = userRep.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException(messages.get("error.resource.notfound", "user", "id", id)));
        user.getRoles().size();
		user.setLastLogin(new Date());
		return userRep.save(user);
	}
	
	@Transactional
	public TokenResponse createUser(@Valid SignUpRequest signup) {
		String[] fullName = generateFullName(signup.getFullName());
		String firstName = fullName != null ? fullName[0] : signup.getFullName();
		String lastName = fullName != null ? fullName[1] : null;
        User user = userRep.save(new User(signup.getEmail(), encoder.encode(signup.getPassword()), firstName, lastName));
        log.debug("Registering new local user account for: {}", user.getEmail());
        accountService.createVerificationToken(user, TokenType.VERIFYEMAIL);
		return tokenProvider.createAccessToken(user.getId());
	}
	
	
	
	public TokenResponse processToken(HttpServletResponse response) {
		String header = response.getHeader("token");
		if (header == null) {
			throw new AuthRequestException(messages.get("error.oauth.authrefused"));
		}
		String accessToken = new String(Base64.getDecoder().decode(header));	
		return new TokenResponse(accessToken);
	}
	
	private String[] generateFullName(String name) {
		if (name != null) {
			String[] splitName = name.split(" ");
			if (splitName.length == 2) return splitName;
		}
		return null;
	}
	
}