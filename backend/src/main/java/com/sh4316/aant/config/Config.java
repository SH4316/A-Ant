package com.sh4316.aant.config;

import com.sh4316.aant.repository.PostRepository;
import com.sh4316.aant.repository.implementation.PostRepositoryImpl;
import com.sh4316.aant.repository.UserRepository;
import com.sh4316.aant.repository.implementation.UserRepositoryImpl;
import com.sh4316.aant.repository.database.MySQLManager;
import com.sh4316.aant.repository.database.SQLManager;
import com.sh4316.aant.service.PostService;
import com.sh4316.aant.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
	private SQLManager sqlManager = new MySQLManager();

	@Bean
	public SQLManager sqlManager() {
		return sqlManager;
	}

	private UserRepository userRepository = new UserRepositoryImpl(sqlManager);
	private UserService userService = new UserService(userRepository);

	@Bean
	public UserRepository userRepository() {
		return userRepository;
	}
	@Bean
	public UserService userService() {
		return userService;
	}

	private PostRepository postRepository = new PostRepositoryImpl(sqlManager);
	private PostService postService = new PostService(postRepository);

	@Bean
	public PostRepository postRepository() {
		return postRepository;
	}
	@Bean
	public PostService postService() {
		return postService;
	}

}
