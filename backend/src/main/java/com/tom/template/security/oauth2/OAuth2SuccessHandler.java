package com.tom.template.security.oauth2;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.tom.template.config.Properties;
import com.tom.template.exception.BadRequestException;
import com.tom.template.security.OAuthUser;
import com.tom.template.security.token.TokenProvider;
import com.tom.template.util.CookieUtils;
import com.tom.template.util.MessageUtils;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private Properties properties;
	private MessageUtils messages;
	private TokenProvider tokenProvider;
    private OAuth2RequestRepository oAuth2RequestRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    	String targetUrl = determineTargetUrl(request, response); 
    	clearAuthenticationAttributes(request, response);
    	String token = tokenProvider.createToken(((OAuthUser) authentication.getPrincipal()).getId());
    	CookieUtils.addCookie(response, "token", token, 20);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
	
    @Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, properties.getCookies().getRedirectCookieName()).map(Cookie::getValue);
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException(messages.getMessage("error.oauth.unauthorisedurl", redirectUri.get()));
        }
       return redirectUri.orElse(getDefaultTargetUrl());
    }
    
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oAuth2RequestRepository.removeAuthRequestCookies(request, response);
    }
    
    private boolean isAuthorizedRedirectUri(String uri) {
    	if (uri.equals(properties.getAuth().getInternalCallbackUri())) {
    		return true;
    	}
        URI clientRedirectUri = URI.create(uri);
        return properties.getAuth().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create((authorizedRedirectUri));
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
    
}
