package com.sh4316.aant.repository;

import com.sh4316.aant.vo.PostVO;
import com.sh4316.aant.vo.dto.PublicPostDTO;
import jakarta.annotation.Nullable;

import java.util.List;

//@Repository
public interface PostRepository {
	/**
	 * @param id article id
	 * @return return a article dto. it may return null if article does not exist.
	 */
	public @Nullable PostVO getPost(String id);

	/**
	 *
	 * @param userId
	 * @return It (may) return "LinkedList" or null if there is no user.
	 */
	List<PostVO> getPosts(String userId);

	List<PublicPostDTO> getPosts(String userId, boolean hasPermission);

	/**
	 * @return Return false if the post does not exist. Others true.
	 */
	boolean updatePost(String postId, String title, String body);

	public String createPost(PostVO post);

	/**
	 * @param id article id;
	 * @return Return true if there exist article and is removed successfully.
	 */
	public boolean deleteArticle(String id);
}
