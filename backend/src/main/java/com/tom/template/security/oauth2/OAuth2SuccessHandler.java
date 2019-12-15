package com.tom.template.security.oauth2;

import java.io.IOException;
import java.net.URI;
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
    	TokenResponse token = tokenProvider.createAccessToken(((OAuthUser) authentication.getPrincipal()).getId());
    	String targetUrl = determineUrl(request, response, token.getAccessToken());
    	System.out.println(targetUrl);
    	if (targetUrl != "UnauthorizedRequestURI") {
    		clearAuthenticationAttributes(request, response);
        	getRedirectStrategy().sendRedirect(request, response, targetUrl);
    	} else {
    		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, messages.get("error.auth.unauthorizedredirect"));
    	}
    }

	private String determineUrl(HttpServletRequest request, HttpServletResponse response, String token) throws IOException {
        Optional<String> redirectUri = CookieUtils.getCookie(request, properties.getCookies().getRedirectCookieName()).map(Cookie::getValue);
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
        	return "UnauthorizedRequestURI";
        }
        return String.format("%s/%s", redirectUri.get(), token);
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
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
    
}
