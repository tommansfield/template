package com.tom.template.security.token;

import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.tom.template.config.Properties;
import com.tom.template.dto.TokenResponse;
import com.tom.template.security.LocalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenProvider {
	
	private final Properties properties;
	
	@Lazy
	@Autowired 
	private AuthenticationManager authenticationManager;
	
	public TokenResponse createToken(Long id) {
		Date expiryDate = new Date(new Date().getTime() + properties.getAuth().getTokenValidityMSecs());
		String token = Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, properties.getAuth().getTokenSecret())
                .compact();
		return new TokenResponse(Base64.getEncoder().encodeToString(token.getBytes()));	
    }		
	
	public TokenResponse createToken(String email, String password) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return createToken(((LocalUser) authentication.getPrincipal()).getId());
	}
	
	public Long getUserIdFromToken(String token) {
		token = new String(Base64.getDecoder().decode(token));
        Claims claims = Jwts.parser()
                .setSigningKey(properties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
	
	public boolean validateToken(String authToken) {
        try {
        	authToken = new String(Base64.getDecoder().decode(authToken));
            Jwts.parser().setSigningKey(properties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
        return false;
        }
    }
	
}
