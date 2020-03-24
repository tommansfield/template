package com.tom.template.service;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.tom.template.entity.User;
import com.tom.template.exception.OAuth2Exception;
import com.tom.template.repository.UserRepository;
import com.tom.template.security.AuthProvider;
import com.tom.template.security.OAuthUser;
import com.tom.template.security.oauth2.userinfo.OAuth2UserInfo;
import com.tom.template.security.oauth2.userinfo.OAuth2UserInfoFactory;
import com.tom.template.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

	private MessageUtils messages;
	private UserRepository userRep;

	@Autowired
	public OAuth2UserService(MessageUtils messages, UserRepository userRep) {
		this.messages = messages;
		this.userRep = userRep;
	}


	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
			return processOAuth2User(oAuth2UserRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws OAuth2Exception {
		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
				oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
		String providerName = oAuth2UserRequest.getClientRegistration().getRegistrationId();
		AuthProvider provider = AuthProvider.valueOf(providerName.toUpperCase());
		if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2Exception(messages.get("error.oauth.emailnotprovided", providerName)); 
		}
		User user;
		Optional<User> existingUser = userRep.findByEmail(oAuth2UserInfo.getEmail());
		if (existingUser.isPresent()) {
			user = existingUser.get();
			if (!user.getProvider().equals(provider)) {
				throw new OAuth2Exception(messages.get("error.oauth.wrongprovider", providerName)); 
			}
			log.debug("Login request for {} user: {}", providerName , user.getEmail());
		} else {
			user = new User(provider);
			log.debug("Registering new {} user account for {}", providerName, oAuth2UserInfo.getEmail());
		}
		createOrUpdateUser(user, oAuth2UserInfo);
		return OAuthUser.create(user);
	}
	
	private void createOrUpdateUser(User user, OAuth2UserInfo userInfo) {
		user.setFirstName(userInfo.getFirstName());
		user.setLastName(userInfo.getLastName());
		user.setEmail(userInfo.getEmail());
		user.setImageUrl(userInfo.getImageUrl());
		user.setLastLogin(new Date());
        userRep.save(user);
    }
	
}
