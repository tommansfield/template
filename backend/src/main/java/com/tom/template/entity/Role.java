package com.tom.template.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter @Setter
public class Role implements Serializable {

	private static final long serialVersionUID = 8357558569615586915L;
	
	public static final Role ADMIN = new Role(1L, "ADMIN");
	public static final Role USER = new Role(2L, "USER");
	public static final Role VERIFIED_USER = new Role(3L, "VERIFIED_USER");
	public static final Role HAS_PASSWORD = new Role(4L, "HAS_PASSWORD");
	public static final Role RESET_PASSWORD = new Role(5L, "RESET_PASSWORD");
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;
	
	@NotEmpty(message = "{error.role.name.empty}")
	@Column(length = 20, nullable = false)
	private String name;
	
	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
	@JsonIgnore @ToString.Exclude
	private Set<User> users = new HashSet<>();

	private Role(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@PreRemove
	public void removeReferences() {
		for (User user : users) {
			user.getRoles().remove(this);
		}
	}

}