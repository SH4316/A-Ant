package com.sh4316.aant.controller;

import com.sh4316.aant.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Value("sh4316.domain")
	private String domain = "error.com";

	private UserService userService;

	public UserController(@Autowired UserService userService) {
		this.userService = userService;
	}

	/**
	 * {<br>
	 * 	   "email": ""<br>
	 *     "pw: ""<br>
	 * }
	 * @param body
	 * @param res
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody String body, HttpServletResponse res) {
		// TODO : 로그인시 https 인지 확인
		JSONObject jsonBody;
		String email;
		String password;

		try {
			jsonBody = new JSONObject(body);
			email = jsonBody.getString("email");
			password = jsonBody.getString("pw");

			if (!userService.checkPassword(email, password)) {
				return ResponseEntity
						.status(HttpStatus.UNAUTHORIZED.value())
						.contentType(MediaType.TEXT_PLAIN)
						.body("Fail to login");
			}


			String jwt = userService.createJWT(email);
			ResponseCookie jwtCookie = ResponseCookie.from("JWT", jwt)
					.maxAge(60*30)
					.path("/")
					.sameSite("None")
					.secure(true)
					.build();
//			jwtCookie.setMaxAge(60*30);
//			jwtCookie.setDomain(domain);
//			jwtCookie.setHttpOnly(true);
//			jwtCookie.setSecure(true);
//			jwtCookie.setAttribute("SameSite", "None");

//			res.addCookie(jwtCookie);
			res.addHeader("Set-Cookie", jwtCookie.toString());
			return ResponseEntity.status(HttpStatus.CREATED.value())
//					.header("Cookie", "JWT=" + jwt)
					.contentType(MediaType.TEXT_PLAIN)
					.body("Login succeed");
		} catch (JSONException e) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST.value())
					.contentType(MediaType.TEXT_PLAIN)
					.body("Invalid JSON format");
		}
		/*
		curl --request POST --header 'Content-Type: application/json;' --data '{"email":"abc@gmail.com","pw":"123"}' --verbose localhost:8080/api/v1/user/login
		 */
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody String body) {
		String email;
		String password;

		JSONObject jsonBody = new JSONObject(body);
		try {
			email = jsonBody.getString("email");
			password = jsonBody.getString("pw");
		} catch (JSONException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body("Invalid JSON format.");
		}

		boolean result = userService.registerUser(email, password, "user");
		if (!result) {
			if (userService.getUserByEmail(email) != null) {
				// TODO : check already exist a account
				return ResponseEntity
						.status(HttpStatus.UNAUTHORIZED.value())
						.contentType(MediaType.TEXT_PLAIN)
						.body("Email already exists");
			}

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
					.contentType(MediaType.TEXT_PLAIN)
					.body("Fail to register user.");
		}

		return ResponseEntity
				.status(HttpStatus.CREATED.value())
				.contentType(MediaType.TEXT_PLAIN)
				.body("Success to create account.");

		/*
		curl --request POST --header 'Content-Type: application/json;' --data '{"email":"abc@gmail.com","pw":"123"}' --verbose localhost:8080/api/v1/user/register
		 */
	}

	@DeleteMapping("/delete")
	public void delete(HttpServletRequest req) {
		// TODO : 정상적인 요청인지 확인하기


	}
}
