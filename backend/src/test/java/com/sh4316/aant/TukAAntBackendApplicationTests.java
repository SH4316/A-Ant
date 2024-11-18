package com.sh4316.aant;

import com.sh4316.aant.post.controller.PostController;
import com.sh4316.aant.post.controller.UserController;
import com.sh4316.aant.post.service.UserService;
import com.sh4316.aant.post.vo.DecodedJwtVO;
import com.sh4316.aant.post.vo.dto.UserDTO;
import jakarta.servlet.http.Cookie;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TukAAntBackendApplicationTests {

	@Autowired UserController userController;
	@Autowired UserService userService;

	@Autowired PostController postController;

	@Test
	void contextLoads() {
		String email = "abcdefg@gmail.com";
		String password = "123456789";
//		userControllerRegister(email, password);
//		userControllerLogin(email, password);
//		userControllerDeleteAccount(email, password);
	}

	void userControllerRegister(String email, String password) {
		userController.register(new JSONObject()
				.put("email", email)
				.put("pw", password)
				.toString()
		);
		UserDTO userDTO = userService.getUserByEmail(email);
		assertNotNull(userDTO);
		Assertions.assertEquals(userDTO.email(), email);

		UserDTO userDTO2 = userService.getUser(userDTO.id());
		assertNotNull(userDTO2);
	}

	void userControllerLogin(String email, String password) {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		userController.login(new JSONObject()
				.put("email", email)
				.put("pw", password)
				.toString()
		, mockHttpServletResponse);

		Cookie cookie = mockHttpServletResponse.getCookie("JWT");
		assertNotNull(cookie);

		UserDTO user = userService.getUserByEmail(email);
		assertNotNull(user);

		Optional<DecodedJwtVO> jwt = userService.validateJWT(cookie.getValue());
		assertTrue(jwt.isPresent());

		JSONObject jsonObject = new JSONObject(jwt.get().getPayload());
		assertEquals(jsonObject.get("email"), email);
		assertEquals(jsonObject.getString("id"), user.id());

	}

	void userControllerDeleteAccount(String email, String password) {

	}
}
