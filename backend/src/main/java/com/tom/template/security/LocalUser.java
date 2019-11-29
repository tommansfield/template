package com.tom.template.security;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.tom.template.entity.User;
import lombok.Getter;

@Getter
public class LocalUser implements UserDetails {

	private static final long serialVersionUID = -49416779380052521L;

	private Long id;
	private String username;
	private String password;
	private boolean isEnabled = true;
	private boolean isAccountNonLocked = true;
	private boolean isAccountNonExpired = true;
	private boolean isCredentialsNonExpired = true;
	
	public Collection<? extends GrantedAuthority> authorities;

	private LocalUser() {}
	
	public static LocalUser create(User user) {
		LocalUser principal = new LocalUser();
		principal.id = user.getId();
		principal.username = String.valueOf(user.getId());
		principal.password = user.getPassword();
		principal.authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName())).collect(Collectors.toList());
		return principal;
	}

}
