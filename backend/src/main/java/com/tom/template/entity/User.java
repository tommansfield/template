package com.tom.template.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tom.template.dto.SignUpRequest;
import com.tom.template.security.AuthProvider;
import com.tom.template.util.TokenType;
import com.tom.template.util.validation.ValidEmail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class User implements Serializable {
	
	private static final long serialVersionUID = -4796836641263652583L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Include
	private Long id;
	
	@Column(nullable = false, unique = true)
	@ValidEmail 
	@ToString.Include
	@EqualsAndHashCode.Include
	private String email;
	
	@Column(unique = true, length = 25)
	private String username;
	
	@Column(length = 15)
	private String firstName;
	
	@Column(length = 20)
	private String lastName;
	
	@JsonIgnore
	private String password;
	
	@NotNull
	@ToString.Include
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String imageUrl;
	
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@EqualsAndHashCode.Include
	private Date created = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval= true)
	private Set<VerificationToken> tokens;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	@JsonIgnore
	private Set<Role> roles;
	
	public User(SignUpRequest signup) {
		this.email = signup.getEmail();
		this.username = signup.getUsername();
		this.password = signup.getPassword();
		this.provider = AuthProvider.LOCAL;
		this.roles = Collections.singleton(Role.USER);
		this.setTokens(Collections.singleton(new VerificationToken(this, TokenType.VERIFYEMAIL)));
	}
	
	public User(AuthProvider provider) {
		this.provider = provider;
		this.roles = Collections.singleton(Role.USER);
		this.setTokens(Collections.singleton(new VerificationToken(this, TokenType.VERIFYEMAIL)));
	}
	
	public User(String username, String email, String password, Set<Role> roles) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.provider = AuthProvider.LOCAL;
		this.roles = roles;
	}
 
	public VerificationToken addToken(VerificationToken token) {
		this.tokens.add(token);
		return token;
	}
	
	public void removeToken(VerificationToken token) {
		this.tokens.remove(token);
	}
	
	
	public boolean hasToken(TokenType tokenType) {
		return tokens.stream().anyMatch(t -> t.getTokenType().equals(tokenType));
	}
	
	public Role addRole(Role role) {
		roles.add(role);
		return role;
	}

	public void removeRole(Role role) {
		roles.remove(role);
	}

	public boolean hasRole(Role role) {
		return roles.contains(role);
	}
	
	@PreRemove
	public void removeReferences() {
		for (Role role : roles) {
			role.getUsers().remove(this);	
		}
	}

}
