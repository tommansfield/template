package com.tom.template.service;

import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tom.template.dto.SignUpRequest;
import com.tom.template.entity.User;
import com.tom.template.exception.ResourceNotFoundException;
import com.tom.template.repository.UserRepository;
import com.tom.template.security.LocalUser;
import com.tom.template.util.MessageUtils;
import com.tom.template.util.validation.ValidEmailValidator;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

	private UserRepository userRep;
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
	
}