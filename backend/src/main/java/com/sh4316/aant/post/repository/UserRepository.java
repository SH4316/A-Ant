package com.sh4316.aant.post.repository;

import com.sh4316.aant.post.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findUserByName(String name);
}
