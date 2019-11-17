package com.tom.template.security.token;

import java.util.Date;
import org.springframework.stereotype.Service;
import com.tom.template.config.Properties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class TokenProvider {
	
	private Properties properties;

	public String createToken(Long id) {
		Date expiryDate = new Date(new Date().getTime() + properties.getAuth().getTokenValidityMSecs());
		return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, properties.getAuth().getTokenSecret())
                .compact();
    }		

	public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(properties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
	
	public boolean validateToken(String authToken) {
        try {
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
