package com.sh4316.aant.post.service;

import org.springframework.stereotype.Service;

@Service
public class PostService {

//	private PostRepository repo;
//
//	public PostService(@Autowired PostRepository repo) {
//		this.repo = repo;
//	}
//
//	/**
//	 *
//	 * @param authorId
//	 * @param title
//	 * @param article
//	 * @return Return -1 if post isn't created, and return the post id.
//	 */
//	public String createPost(String authorId, String title, String article) {
//		// TODO : article id(int) 리턴하게 만들기
//		return repo.createPost(new PostVO(null, authorId, title, article, false, new CommentDTO[0]));
//	}
//
//	/**
//	 * @param postId Id of post
//	 * @param title Title of the post
//	 * @param body Body of the post
//	 * @return Return false if there is no post match with 'postId'
//	 */
//	public boolean updatePost(String postId, String title, String body) {
//		return repo.updatePost(postId, title, body);
//	}
//
//	/**
//	 * @param id article id
//	 * @return
//	 */
//	public Optional<PostVO> getPost(String id) {
//		return Optional.ofNullable(repo.getPost(id));
//	}
//	public List<PostVO> getPosts(String userId) {
//		// TODO : repo에서 권한 있는 파일만 가져올 수 있는 구현 후 수정하기
//		return repo.getPosts(userId);
//	}
//
//	public PublicPostDTO getPublicPost(int id) {
//		// TODO : public post 구현
//		return null;
//	}

//	public List<CommentDTO> getComments(String postId) {
//
//	}
//
//	public List<CommentDTO> getComments(String postId, int length) {
//
//	}
}
