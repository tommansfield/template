package com.tom.template.security;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.tom.template.entity.User;
import lombok.Getter;

@Getter
public class OAuthUser implements OAuth2User {
	
	private Long id;
	private String name;
	private Collection<GrantedAuthority> authorities;
	private Map<String, Object> attributes;
	
	private OAuthUser(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static OAuthUser create(User user) {
		return new OAuthUser(user.getId(), user.getEmail());
	}

}
