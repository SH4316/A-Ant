package com.sh4316.aant.userauth.controller;

import com.sh4316.aant.userauth.dto.EmailAuthDto;
import com.sh4316.aant.userauth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserAuthController {

	@Autowired
	private AuthService authService;

	@GetMapping
	public ResponseEntity<?> emailLogin(@RequestHeader(value = "email", defaultValue = "") String email, @RequestHeader(value = "password", defaultValue = "") String password) {
		if (email.length() == 0 || password.length() == 0) {
			return ResponseEntity.badRequest().build();
		}

		EmailAuthDto dto = new EmailAuthDto(email, password);
		if (authService.emailAuth(dto)) {
			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					// TODO : Login Token 생성
					.header("Set-Cookie", "login-cookie: cookie;")
					.body("Authentication success");
		}
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body("Authentication fail");
	}

	@PutMapping("/normal/password")
	public void passwordChange(@RequestBody EmailAuthDto emailAuthDto) {

	}

	@PostMapping("/normal")
	public ResponseEntity<?> createEmailLogin(@RequestBody EmailAuthDto emailAuthDto) {
		return ResponseEntity.ok("OK");
	}

}
