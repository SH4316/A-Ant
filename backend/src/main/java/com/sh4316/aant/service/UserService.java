package com.sh4316.aant.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sh4316.aant.vo.DecodedJwtVO;
import com.sh4316.aant.vo.dto.UserDTO;
import com.sh4316.aant.repository.UserRepository;
import com.sh4316.aant.utils.Encoding;
import jakarta.servlet.http.Cookie;
import lombok.NonNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class UserService {

	@Value("sh4316.secretKey")
	private String secretKey;
	@Value("sh4316.issuer")
	private String issuer;

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
	public boolean removeUser(String id, String jwt) {
		Optional<DecodedJwtVO> decodedJwtVO = validateJWT(jwt);
		if (decodedJwtVO.isEmpty()) {
			return false;
		}
		try {
			if (!new JSONObject(decodedJwtVO.get().payload()).get("id").equals(id)) {
				return false;
			}
		} catch (JSONException e) {
			return false;
		}
		return repo.deleteUser(id);
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

	/**
	 *
	 * @param email
	 * @return payload contains id, email, and type(user type)/
	 */
	public String createJWT(@NonNull String email) {
//		String id = repo.getUserId(email);
		Date expired = new Date(System.currentTimeMillis() + (1000*60*30));
		UserDTO user = repo.getUser(email);
		if (user == null) {
			return null;
		}
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		return JWT.create()
				.withIssuer(issuer)
				.withExpiresAt(expired)
				.withClaim("id", user.id())
				.withClaim("email", user.email())
				.withClaim("type", user.userType())
				.sign(algorithm);
	}
	public Optional<DecodedJwtVO> validateJWT(String jwt) {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		JWTVerifier verifier = JWT.require(algorithm).build();
		try {
			DecodedJWT decodedJWT = verifier.verify(jwt);
			if (decodedJWT == null) {
				return Optional.empty();
			}
			return Optional.of(new DecodedJwtVO(Encoding.decodeBase64(decodedJWT.getHeader()), Encoding.decodeBase64(decodedJWT.getPayload())));
		} catch (SignatureVerificationException e) {
			// TODO : 구현 (When fail to verify jwt)
			return Optional.empty();
		} catch (TokenExpiredException e) {
			return Optional.empty();
		}
	}

	/**
	 * @return Return false if cookies or filter is null and filter return false.
	 */
	public boolean checkJWT(Cookie[] cookies, Predicate<JSONObject> filter) {
		if (cookies == null || filter == null) {
			return false;
		}
		for (Cookie c : cookies) {
			if (!c.getName().equals("JWT"))
				continue;
			Optional<DecodedJwtVO> jwt = validateJWT(c.getValue());
			if (jwt.isEmpty()) {
				continue;
			}
			try {
				return filter.test(new JSONObject(jwt.get().getPayload()));
			} catch (JSONException e) {
				return false;
			}
		}
		return false;
	}

	/**
	 * @param userType Name of user type.
	 * @return Return user_type number. If there are no user type, then return -1.
	 */
	public int getUserType(String userType) {
		return repo.getUserType(userType);
	}
}