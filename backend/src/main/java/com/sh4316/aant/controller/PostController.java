package com.sh4316.aant.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sh4316.aant.vo.PostDTO;
import com.sh4316.aant.vo.dto.PublicPostDTO;
import com.sh4316.aant.service.PostService;
import com.sh4316.aant.service.UserService;
import com.sh4316.aant.utils.Encoding;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

	private final PostService postService;
	private final UserService userService;
	public PostController(@Autowired PostService postService, @Autowired UserService userService) {
		this.postService = postService;
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<?> getPostTest(HttpServletRequest req) {
		return ResponseEntity.ok()
				.contentType(MediaType.TEXT_PLAIN)
				.body("Text Data");
		/*
		curl --request GET --verbose localhost:8080/api/v1/post
		 */
	}
	@GetMapping("/{id}")
	public ResponseEntity<String> getPost(HttpServletRequest req, @PathVariable int id) {
		// TODO : 유저 인증 단계 추가하기 (필요한 경우에)
		// TODO : getPublicPost(id) 사용하기
		PostDTO post = postService.getPost(id);
		if (post == null) {
			return ResponseEntity.status(404).body("No article");
		}
		return ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(new PublicPostDTO(
						post.getId(),
						post.getTitle(),
						post.getBody(),
						post.getComments()
				).toJSONString());
		/*
		curl --request GET --verbose localhost:8080/api/v1/post/4
		 */
	}
	@PostMapping
	public ResponseEntity<String> registerPost(@RequestBody String body, HttpServletRequest req, HttpServletResponse res) {
		// TODO : body를 mapping object 로 받기 (에러 헨들링 필요)

		String author;
		String title;
		String postBody;
		try {
			JSONObject json = new JSONObject(body);
			author = json.getString("author");
			title = json.getString("title");
			postBody = json.getString("body");
		} catch (JSONException e) {
//			res.setStatus(HttpStatus.BAD_REQUEST.value());
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST.value())
					.contentType(MediaType.TEXT_PLAIN)
					.body("Invalidate JSON format (" + body + ")");
		}
		try {
			boolean verified = false;
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					if (c.getName().equals("JWT")) {
						// TODO : optional 공부 해서 다르게 만들기
						DecodedJWT jwt = userService.validateJWT(c.getValue()).get();
						JSONObject payload = new JSONObject(Encoding.decodingBase64(jwt.getPayload()));
						if (!payload.getString("id").equals(author)) {
							return ResponseEntity
									.status(HttpStatus.BAD_REQUEST.value())
									.contentType(MediaType.TEXT_PLAIN)
									.body("Invalid user id");
						}
						verified = true;
					}
				}
			}

			if (!verified) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body("Login Required");
			}
			if (!postService.createPost(author, title, postBody)) {
//				res.setStatus(HttpStatus.BAD_REQUEST.value());
				return ResponseEntity.status(400).body("Error");
			}
//			res.setStatus(HttpStatus.CREATED.value());
			return ResponseEntity.status(201).body("Post created");
		} catch (NoSuchElementException e) {
			// TODO : optional 공부 해서 다르게 만들기
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body("Cannot verify JWT token");
		} catch (JSONException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body("Invalid JWT payload");
		}

		/*
		curl --request POST --header 'Content-Type: application/json' --data '{"author":"SN25PwswxHVCP+F3gykSxbzc48xyhy+AUWJ5Z+8njgg=","title":"hello","article":"world!"}' --verbose localhost:8080/api/v1/post
		curl --request POST --header 'Cookie: JWT=' --header 'Content-Type: application/json' --data '{"author":"SN25PwswxHVCP+F3gykSxbzc48xyhy+AUWJ5Z+8njgg=","title":"hello","body":"world!"}' --verbose localhost:8080/api/v1/post
		 */

	}
	@GetMapping("/commnet")
	public ResponseEntity<String> getCommnets(HttpServletRequest req) {
		return ResponseEntity.ok()
				.header("Content-Type", "text/plain")
				.body("Test2");
	}
}


