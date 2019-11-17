package com.tom.template.security.oauth2;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private OAuth2RequestRepository oAuth2RequestRepository;
	
	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String targetUrl = UriComponentsBuilder.fromUriString("/auth/callbackerror").queryParam("error", exception.getLocalizedMessage()).build().toUriString();
        oAuth2RequestRepository.removeAuthRequestCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
	
}
