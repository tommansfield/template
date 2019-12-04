package com.tom.template.security.token;

import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.tom.template.config.Properties;
import com.tom.template.dto.TokenResponse;
import com.tom.template.security.LocalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
	
	private final Properties properties;
	
	@Lazy
	@Autowired 
	private AuthenticationManager authenticationManager;
	
	public String createToken(Long id) {
		Date expiryDate = new Date(new Date().getTime() + properties.getAuth().getTokenValidityMSecs());
		return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, properties.getAuth().getTokenSecret())
                .compact();
    }		
	
	public TokenResponse createToken(String email, String password) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = createToken(((LocalUser) authentication.getPrincipal()).getId());
        return new TokenResponse(Base64.getEncoder().encodeToString(token.getBytes()));
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
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
	
}
