package com.sh4316.aant.repository;

import com.sh4316.aant.vo.PostDTO;
import jakarta.annotation.Nullable;

//@Repository
public interface PostRepository {
	/**
	 * @param id article id
	 * @return return a article dto. it may return null if article does not exist.
	 */
	public @Nullable PostDTO getPost(int id);

	/**
	 * @return Return false if the post does not exist. Others true.
	 */
	boolean updatePost(int postId, String title, String body);

	public boolean createPost(PostDTO post);

	/**
	 * @param id article id;
	 * @return Return true if there exist article and is removed successfully.
	 */
	public boolean deleteArticle(int id);
}
