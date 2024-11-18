package com.sh4316.aant.post.utils;

import com.mysql.cj.exceptions.NumberOutOfRange;
import com.sh4316.aant.userauth.domain.EmailLogin;
import lombok.Getter;

public enum SignInType {

	EMAIL((byte) 1, EmailLogin.class),;

	@Getter
	private final byte value;
	@Getter
	private final Class<?> entityClass;

	SignInType(byte id, Class<?> entityClass) {
		if (id < 1 || id > 32) {
			throw new NumberOutOfRange("'LoginType' must have an id value less than 32 and greater than 1.");
		}
		this.value = id;
		this.entityClass = entityClass;
	}
}
