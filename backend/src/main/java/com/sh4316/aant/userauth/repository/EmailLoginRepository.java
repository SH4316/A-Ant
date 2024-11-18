package com.sh4316.aant.userauth.repository;

import com.sh4316.aant.userauth.domain.EmailLogin;
import com.sh4316.aant.post.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailLoginRepository extends JpaRepository<EmailLogin, Long> {
	Optional<EmailLogin> findByUser(User user);
}
