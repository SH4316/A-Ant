package com.sh4316.aant.service;

import com.sh4316.aant.vo.PostDTO;
import com.sh4316.aant.vo.dto.CommentDTO;
import com.sh4316.aant.repository.PostRepository;
import com.sh4316.aant.vo.dto.PublicPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

	private PostRepository repo;

	public PostService(@Autowired PostRepository repo) {
		this.repo = repo;
	}

	public boolean createPost(String authorId, String title, String article) {
		// TODO : article id(int) 리턴하게 만들기
		return repo.createPost(new PostDTO(-1, authorId, title, article, new CommentDTO[0]));
	}

	/**
	 * @param postId Id of post
	 * @param title Title of the post
	 * @param body Body of the post
	 * @return Return false if there is no post match with 'postId'
	 */
	public boolean updatePost(int postId, String title, String body) {
		// TODO : 구현
		return repo.updatePost(postId, title, body);
	}

	/**
	 * @param id article id
	 * @return
	 */
	public PostDTO getPost(int id) {
		return repo.getPost(id);
	}

	public PublicPostDTO getPublicPost(int id) {
		// TODO : public post 구현
		return null;
	}

}
