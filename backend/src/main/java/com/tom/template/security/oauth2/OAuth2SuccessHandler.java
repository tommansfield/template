package com.tom.template.security.oauth2;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.tom.template.config.Properties;
import com.tom.template.dto.TokenResponse;
import com.tom.template.exception.BadRequestException;
import com.tom.template.security.OAuthUser;
import com.tom.template.security.token.TokenProvider;
import com.tom.template.util.CookieUtils;
import com.tom.template.util.MessageUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final Properties properties;
	private final MessageUtils messages;
	private final TokenProvider tokenProvider;
    private final OAuth2RequestRepository oAuth2RequestRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    	String targetUrl = determineTargetUrl(request, response); 
    	clearAuthenticationAttributes(request, response);
    	TokenResponse token = tokenProvider.createAccessToken(((OAuthUser) authentication.getPrincipal()).getId());
    	response.setHeader("token", Base64.getEncoder().encodeToString(token.getAccessToken().getBytes()));
    	request.getRequestDispatcher(targetUrl).forward(request, response);
    }
	
    @Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, properties.getCookies().getRedirectCookieName()).map(Cookie::getValue);
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException(messages.get("error.oauth.unauthorisedurl", redirectUri.get()));
        }
       return redirectUri.orElse(getDefaultTargetUrl());
    }
    
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oAuth2RequestRepository.removeAuthRequestCookies(request, response);
    }
    
    private boolean isAuthorizedRedirectUri(String uri) {
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
