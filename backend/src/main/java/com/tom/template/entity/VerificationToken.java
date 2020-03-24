package com.tom.template.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tom.template.util.TokenType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "verification_tokens")
@Getter @Setter
@NoArgsConstructor
public class VerificationToken implements Serializable {
	
	static final long serialVersionUID = -3781711994555399422L;
	private static final int EXPIRATION = 60 * 24;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
    private User user;
	
	@Column(unique = true)
	private String token;
	
	@NotNull
	private TokenType tokenType;

	@Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
	
	private static Date addMinutesToDate() {
		Date date = new Date();
	    final long ONE_MINUTE_IN_MILLIS = 60000;
	    long timeInMs = date.getTime();
	    return new Date(timeInMs + (EXPIRATION * ONE_MINUTE_IN_MILLIS));
	}
	
	public VerificationToken(User user, TokenType tokenType) {
		this.user = user;
		this.tokenType = tokenType;
		this.token = UUID.randomUUID().toString();
		this.expirationDate = addMinutesToDate();
	}
	
	public boolean matches(String token) {
		return this.token.equals(token);
	}
	
}
