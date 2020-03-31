package com.tom.template.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.template.entity.User;
import com.tom.template.entity.VerificationToken;
import com.tom.template.util.TokenType;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
	
	Optional<VerificationToken> findByUserAndTokenType(User user, TokenType tokenType);
	
}
