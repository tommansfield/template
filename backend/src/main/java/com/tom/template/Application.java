package com.tom.template;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tom.template.config.Properties;
import com.tom.template.entity.Role;
import com.tom.template.entity.User;
import com.tom.template.repository.RoleRepository;
import com.tom.template.repository.UserRepository;

@SpringBootApplication
@EnableConfigurationProperties(Properties.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRep, UserRepository userRep, BCryptPasswordEncoder encoder) {
		return (args) -> {
			if (roleRep.count() == 0) {
				List<Role> roles = Arrays.asList(Role.ADMIN, Role.USER, Role.VERIFIED_USER, Role.RESET_PASSWORD);
				roleRep.saveAll(roles);
			}
			if (userRep.count() == 0) {
				userRep.save(new User("admin@admin.com", encoder.encode("password"),
						new HashSet<>(Arrays.asList(Role.ADMIN, Role.USER))));
			}
		};
	}
	
}
