package com.tom.template.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app")
@Getter @Setter
public class Properties {

	private String name;
	private String host;
	private String swaggerUrl;
	private final Auth auth = new Auth();
	private final Cookies cookies = new Cookies();
	private final Mail mail = new Mail();
	
	@Getter @Setter
	public static class Auth {
		private String clientId;
		private String secret;
		private String tokenSecret;
		private String[] grantTypes;
		private String[] scopes;
		private int tokenValidityMSecs;
		private String internalCallbackUri;
		private List<String> authorizedRedirectUris = new ArrayList<>();
	}
	
	@Getter @Setter
	public static class Cookies {
		private String oAuthCookieName;
		private String redirectCookieName;
		private int oAuthCookieTimeout;
	}
	
	@Getter @Setter
	public static class Mail {
		private String username;
		private String password;
		private String host;
		private String port;
	}
	
}