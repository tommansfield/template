package com.tom.template.service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.tom.template.dto.SignUpRequest;
import com.tom.template.entity.User;
import com.tom.template.exception.ResourceNotFoundException;
import com.tom.template.repository.UserRepository;
import com.tom.template.security.LocalUser;
import com.tom.template.util.MessageUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final MessageUtils messages;
	private final UserRepository userRep;
	
	@Lazy
	@Autowired 
	private PasswordEncoder encoder;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		System.out.println("here");
		User user = userRep.findByEmail(login).orElseThrow(() 
				-> new UsernameNotFoundException(messages.get("error.login.emailnotfound", login)));
			return LocalUser.create(user);
	}

	@Transactional
    public User loadUserById(Long id) {
        User user = userRep.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException(messages.get("error.resource.notfound", "user", "id", id)));
        return user;
	}

	@Transactional
	public User createUser(@Valid SignUpRequest signup) {
		String[] fullName = generateFullName(signup.getFullName());
		String firstName = fullName != null ? fullName[0] : signup.getFullName();
		String lastName = fullName != null ? fullName[1] : null;
        return userRep.save(new User(signup.getEmail(), encoder.encode(signup.getPassword()), firstName, lastName));
	}
	
	private String[] generateFullName(String name) {
		if (name != null) {
			String[] splitName = name.split(" ");
			if (splitName.length == 2) return splitName;
		}
		return null;
	}
	
}