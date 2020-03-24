package com.tom.template.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tom.template.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Override
	Optional<User> findById(Long id);
	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
	// Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
	
}
