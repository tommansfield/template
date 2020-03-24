package com.tom.template.security.oauth2;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.tom.template.util.MessageUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private final OAuth2RequestRepository oAuth2RequestRepository;
	private final MessageUtils messages;
	
	@SneakyThrows
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        oAuth2RequestRepository.removeAuthRequestCookies(request, response);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, messages.get("error.oauth.authrefused"));
    }
	
}
