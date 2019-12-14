package com.tom.template.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.tom.template.security.AuthEntryPoint;
import com.tom.template.security.oauth2.OAuth2FailureHandler;
import com.tom.template.security.oauth2.OAuth2RequestRepository;
import com.tom.template.security.oauth2.OAuth2SuccessHandler;
import com.tom.template.security.token.TokenAuthFilter;
import com.tom.template.service.OAuth2UserService;
import com.tom.template.service.UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UserService userService;
	private final OAuth2UserService oAuth2UserService;
	private final OAuth2SuccessHandler successHandler;
	private final OAuth2FailureHandler failureHandler;
	private final TokenAuthFilter tokenAuthenticationFilter;
	private final OAuth2RequestRepository oAuth2RequestRepository;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthEntryPoint authEntryPoint() {
		return new AuthEntryPoint();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(encoder());
	}

	@Override
	  protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
	    http
	    	.cors()
	    	.and()
	    	.csrf().disable()
	    	.httpBasic().disable()
	    	.formLogin().disable()
	    	.headers().frameOptions().disable()
	    	.and()
	    	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    	.and()
	    	.exceptionHandling().authenticationEntryPoint(authEntryPoint())
	    	.and()
	    	.authorizeRequests()
	    		.antMatchers("/**/**.ico", "/**/**.png","/**/**.jpg", "/**/**.css", "/**/**.js", "/**/**.html").permitAll()
	    		.antMatchers("/error/**", "/auth/**").permitAll()
    			.antMatchers("/console/**", "/admin").hasAnyRole("ADMIN")
    			.antMatchers("/api/**").authenticated()
    		.and()
   			.oauth2Login()
	   			.authorizationEndpoint().baseUri("/oauth2/authorize")
	   			.authorizationRequestRepository(oAuth2RequestRepository)
	   			.and()
	   			.redirectionEndpoint().baseUri("/oauth2/callback/*")
	   			.and()
	   			.userInfoEndpoint().userService(oAuth2UserService)
	   			.and()
	   			.successHandler(successHandler)
	   			.failureHandler(failureHandler);
	    // @formatter:on
	    http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	  }
}