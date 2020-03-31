package com.tom.template.security.oauth2.userinfo;

import java.util.Map;

import com.tom.template.exception.OAuth2Exception;
import com.tom.template.security.AuthProvider;

public class OAuth2UserInfoFactory {
	
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) throws OAuth2Exception {
		
		if (registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())) {
            return new FacebookUserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
        	return new GoogleUserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
        	return new GithubUserInfo(attributes);
        } else {
            throw new OAuth2Exception(String.format("Login with %s is not supported yet.", registrationId));
        }
    }

}