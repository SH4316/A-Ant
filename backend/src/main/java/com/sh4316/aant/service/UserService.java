package com.sh4316.aant.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sh4316.aant.vo.UserDTO;
import com.sh4316.aant.repository.UserRepository;
import com.sh4316.aant.utils.Encoding;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;

public class UserService {
	private UserRepository repo;

	public UserService(@Autowired UserRepository repo) {
		this.repo = repo;
	}

	public UserDTO getUserByEmail(String email) {
		return repo.getUser(email);
	}
	public UserDTO getUser(String id) {
		// TODO : 구현
		return null;
	}
	public boolean registerUser(String email, String password, String userType) {
		return registerUser(email, password, repo.getUserType(userType));
	}

	/**
	 *
	 * @param email Email
	 * @param password Not encoded password.
	 * @param userType get user-type number from getUserType(String) in UserRepository.
	 * @return Succeed
	 */
	public boolean registerUser(String email, String password, int userType) {
		// TODO : 유저가 이미 존재하는지 확인 후 등록
		// TODO : 유저 등록시 이메일 형식 확인
		String id = repo.registerUser(email, Encoding.encoding(password), userType);
		return id != null;
	}
	public boolean checkPassword(String email, String password) {
		String pw = repo.getPassword(email);
		if (pw == null)
			return false;
		return Encoding.encoding(password).equals(pw);
	}
	public boolean checkPasswordWithId(String id, String password) {
		// TODO : 구현
		return false;
	}
	public String createJWT(@NonNull String email) {
		String id = repo.getUserId(email);
		if (id == null) {
			return null;
		}
		// TODO : jwt key 하드코딩 x
		Algorithm algorithm = Algorithm.HMAC256("a;eobrh4t3p9qh");
		return JWT.create()
				.withIssuer("popu.kr")
				.withExpiresAt(new Date(System.currentTimeMillis() + (1000*60*30)))
				.withClaim("id", id)
				.withClaim("email", email)
				.sign(algorithm);
	}
	public Optional<DecodedJWT> validateJWT(String jwt) {
		// TODO : jwt key 하드코딩 x
		Algorithm algorithm = Algorithm.HMAC256("a;eobrh4t3p9qh");
		JWTVerifier verifier = JWT.require(algorithm).build();
		try {
			return Optional.of(verifier.verify(jwt));
		} catch (SignatureVerificationException e) {
			// TODO : 구현 (When fail to verify jwt)
			return Optional.empty();
		} catch (TokenExpiredException e) {
			return Optional.empty();
		}
	}

	/**
	 * @param userType Name of user type.
	 * @return Return user_type number. If there are no user type, then return -1.
	 */
	public int getUserType(String userType) {
		return repo.getUserType(userType);
	}
}