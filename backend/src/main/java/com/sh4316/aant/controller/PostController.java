package com.sh4316.aant.controller;

import com.sh4316.aant.service.PostService;
import com.sh4316.aant.service.UserService;
import com.sh4316.aant.vo.DecodedJwtVO;
import com.sh4316.aant.vo.PostVO;
import com.sh4316.aant.vo.dto.PublicPostDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
	public ResponseEntity<String> getPost(HttpServletRequest req, @PathVariable String id) {
		// TODO : 유저 인증 단계 추가하기 (필요한 경우에)
		// TODO : getPublicPost(id) 사용하기
		Optional<PostVO> post1 = postService.getPost(id);
		if (post1.isEmpty()) {
			return ResponseEntity
					.status(404)
					.contentType(MediaType.TEXT_PLAIN)
					.body("No article");
		}
		PostVO post = post1.get();
		if (!post.isPublished()) {
			List<Cookie> list = null;
			if (req.getCookies() != null) {
				list = Arrays.stream(req.getCookies()).filter(v -> v.getName().equals("JWT")).toList();
			}
			if (list == null || list.size() == 0) {
				return ResponseEntity
						.status(HttpStatus.UNAUTHORIZED)
						.contentType(MediaType.TEXT_PLAIN)
						.body("No JWT token");
			}
			String jwt = list.get(0).getValue();
			userService.validateJWT(jwt);
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
	@GetMapping("/all")
	public ResponseEntity<?> getPosts(HttpServletRequest req, @RequestParam(name = "user") String userId) {
		List<PostVO> posts = postService.getPosts(userId);
		if (posts == null) {
			return ResponseEntity
					.badRequest()
					.contentType(MediaType.TEXT_PLAIN)
					.body("There exists no user " + userId);
		}
		var ref = new Object() {
			boolean hasPermission = false;
		};

		// TODO : checkJWT() 로 바꾸기
		if (req.getCookies() != null) {
			for (Cookie c : req.getCookies()) {
				if (!c.getName().equals("JWT")) {
					continue;
				}
				userService.validateJWT(c.getValue()).ifPresent(c1 -> {
					try {
						JSONObject payload = new JSONObject(c1.getPayload());
						if (payload.get("id").equals(userId)) {
							ref.hasPermission = true;
						}
					} catch (JSONException e) {
					}
				});
			}
		}
		JSONObject result = new JSONObject();
		JSONArray resultPosts = new JSONArray();

		for (PostVO p : posts) {
			if (!p.isPublished() && !ref.hasPermission) {
				continue;
			}
			resultPosts.put(new PublicPostDTO(p.getId(), p.getTitle(), p.getBody(), p.getComments()));
		}

		result.put("verified", ref.hasPermission);
		result.put("posts", resultPosts);
		return ResponseEntity
				.ok().
				contentType(MediaType.APPLICATION_JSON)
				.body(result.toString());
	}
	@PostMapping
	public ResponseEntity<String> registerPost(@RequestBody String body, HttpServletRequest req, HttpServletResponse res) {
		// TODO : body를 mapping object 로 받기 (에러 헨들링 필요)

		String author = null;
		String title;
		String postBody;
		JSONObject json;
		try {
			json = new JSONObject(body);
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
			author = json.getString("author");
		} catch (JSONException e) {
		}
		try {
			boolean verified = false;
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				// TODO : checkJWT() 로 바꾸기
				for (Cookie c : cookies) {
					if (c.getName().equals("JWT")) {
						// TODO : optional 공부 해서 다르게 만들기
						DecodedJwtVO jwt = userService.validateJWT(c.getValue()).get();
						JSONObject payload = new JSONObject(jwt.getPayload());
						if (author == null) {
							author = payload.getString("id");
						} else if (!payload.getString("id").equals(author)) {
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
			String id = postService.createPost(author, title, postBody);
			if (id == null) {
				return ResponseEntity.status(400).body("Error");
			}
//			res.setStatus(HttpStatus.CREATED.value());
			return ResponseEntity.status(201).contentType(MediaType.TEXT_PLAIN).body(id);
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

	/**
	 * Custom headers:<br>
	 * Post-ID : post id
	 * Post-Title : title of post
	 * Post-Body : content of post
	 */
	@PutMapping()
	public ResponseEntity<?> updateArticle(HttpServletRequest req) {
//		String author = req.getHeader("Post-Author");
		String author;
		String postId = req.getHeader("Post-ID");
		String title = req.getHeader("Post-Title");
		String body = req.getHeader("Post-Body");

		if (postId == null || title == null || body == null) {
			return ResponseEntity
					.badRequest()
					.contentType(MediaType.TEXT_PLAIN)
					.body("Missing header (Post-ID, Post-Title, or Post-Body)");
		}

		Optional<PostVO> post = postService.getPost(postId);
		if (post.isEmpty()) {
			return ResponseEntity
					.notFound()
					.build();
		}
		author = post.get().getAuthorId();

		if (!userService.checkJWT(req.getCookies(), j -> j.getString("id").equals(author))) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.contentType(MediaType.TEXT_PLAIN)
					.body("Invalid JWT");
		}

		boolean result = postService.updatePost(postId, title, body);
		if (!result) {
			return ResponseEntity
					.badRequest()
					.contentType(MediaType.TEXT_PLAIN)
					.body("There is no post matching the provided post ID");
		}
		return ResponseEntity.ok("Succeed to update");
	}

	@GetMapping("/commnet")
	public ResponseEntity<String> getCommnets(HttpServletRequest req) {
		return ResponseEntity.ok()
				.header("Content-Type", "text/plain")
				.body("Test2");
	}
}


