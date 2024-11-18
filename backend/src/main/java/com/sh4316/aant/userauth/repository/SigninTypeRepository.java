package com.sh4316.aant.userauth.repository;

import com.sh4316.aant.userauth.domain.SigninType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigninTypeRepository extends JpaRepository<SigninType, Long> {
}
