package com.tom.template.security.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.tom.template.config.Properties;
import com.tom.template.util.CookieUtils;

@Component
public class OAuth2RequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	@Autowired
	private Properties properties;

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authRequest, HttpServletRequest request,
			HttpServletResponse response) {
		String authRequestCookie = properties.getCookies().getOAuthCookieName();
		String redirectCookie = properties.getCookies().getRedirectCookieName();
		int cookieExpireSeconds = properties.getCookies().getOAuthCookieTimeout();
		if (authRequest == null) {
			removeAuthRequestCookies(request, response);
		} else {
			CookieUtils.addCookie(response, authRequestCookie, CookieUtils.serialize(authRequest), cookieExpireSeconds);
			String redirectUri = request.getParameter(redirectCookie);
			if (!StringUtils.isEmpty(redirectUri)) {
				CookieUtils.addCookie(response, redirectCookie, redirectUri, cookieExpireSeconds);
			}
		}
	}

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return CookieUtils.getCookie(request, properties.getCookies().getOAuthCookieName())
				.map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		return this.loadAuthorizationRequest(request);
	}

	public void removeAuthRequestCookies(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.deleteCookie(request, response, properties.getCookies().getOAuthCookieName());
		CookieUtils.deleteCookie(request, response, properties.getCookies().getRedirectCookieName());
	}
}
