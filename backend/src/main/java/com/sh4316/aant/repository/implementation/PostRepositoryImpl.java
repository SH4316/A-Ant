package com.sh4316.aant.repository.implementation;

import com.sh4316.aant.repository.PostRepository;
import com.sh4316.aant.vo.PostVO;
import com.sh4316.aant.vo.dto.CommentDTO;
import com.sh4316.aant.repository.database.SQLConnection;
import com.sh4316.aant.repository.database.SQLManager;
import com.sh4316.aant.vo.dto.PublicPostDTO;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PostRepositoryImpl implements PostRepository {

	private SQLManager sqlManager;

	public PostRepositoryImpl(@Autowired SQLManager sqlManager) {
		this.sqlManager = sqlManager;
	}

	@PostConstruct
	public void init() {
		// TODO : Connection pool 만들기
		Connection conn = null;
		Statement stat = null;

		SQLConnection con = sqlManager.newConnection();
		conn = con.conn();
		stat = con.stat();


		try {
			stat.execute("USE aant");
			stat.execute("CREATE TABLE IF NOT EXISTS Posts (" +
					"id VARCHAR(37) PRIMARY KEY," +
					"author_id VARCHAR(45)," +
					"title TEXT NOT NULL," +
					"body TEXT NOT NULL," +
					"published BOOLEAN NOT NULL," +
					"comments TEXT," +
					"comment_list TEXT," +
					"FOREIGN KEY (author_id) REFERENCES Users(id)" +
					");");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Nullable
	@Override
	public PostVO getPost(String aid) {
		Connection conn;
		Statement stat;

		SQLConnection con = sqlManager.newConnection();
		conn = con.conn();
		stat = con.stat();

		ResultSet resultSet = null;
		try {
			stat.execute("USE aant");
			resultSet = stat.executeQuery("SELECT id, author_id, title, body, published, comments, comment_list FROM Posts WHERE Posts.id = \"" + aid + "\";");

			if (!resultSet.next()) {
				conn.close();
				return null;
			}
			String id = resultSet.getString(1);
			String author_id = resultSet.getString(2);
			String title = resultSet.getString(3);
			String body = resultSet.getString(4);
			Boolean published = resultSet.getBoolean(5);
			String comments = resultSet.getString(6);
			String comment_list = resultSet.getString(7);
			JSONArray jsonComments = new JSONArray(comments);
			JSONArray jsonCommentList = new JSONArray(comment_list);

			// TODO : Comment 기능 구현
			CommentDTO[] commentList = new CommentDTO[jsonCommentList.length()];
			int i;
			for (i = 0; i < jsonComments.length(); i++) {
				JSONObject obj = jsonComments.getJSONObject(i);
				commentList[i] = new CommentDTO(obj.getInt("id"), obj.getString("body"));
			}
			for (; i < jsonCommentList.length(); i++) {
				commentList[i] = new CommentDTO(jsonCommentList.getInt(i));
			}

			conn.close();
			return new PostVO(id, author_id, title, body, published, commentList);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}

	}

	/**
	 *
	 * @param userId
	 * @return Return "LinkedList", or return null if there is no user.
	 */
	@Override
	public List<PostVO> getPosts(String userId) {
		Connection conn;
		Statement stat;

		SQLConnection con = sqlManager.newConnection();
		conn = con.conn();
		stat = con.stat();

		ResultSet resultSet = null;
		try {
			stat.execute("USE aant");
			// TODO : select 갯수 제한하기
			resultSet = stat.executeQuery("SELECT id, author_id, title, body, published, comments, comment_list FROM Posts WHERE Posts.author_id='" + userId + "';");
			List<PostVO> result = new LinkedList<>();

			while (resultSet.next()) {
				String id = resultSet.getString(1);
				String author_id = resultSet.getString(2);
				String title = resultSet.getString(3);
				String body = resultSet.getString(4);
				Boolean published = resultSet.getBoolean(5);
				String comments = resultSet.getString(6);
				String comment_list = resultSet.getString(7);
				JSONArray jsonComments = new JSONArray(comments);
				JSONArray jsonCommentList = new JSONArray(comment_list);

				// TODO : Comment 기능 구현
				CommentDTO[] commentList = new CommentDTO[jsonCommentList.length()];
				int i;
				for (i = 0; i < jsonComments.length(); i++) {
					JSONObject obj = jsonComments.getJSONObject(i);
					commentList[i] = new CommentDTO(obj.getInt("id"), obj.getString("body"));
				}
				for (; i < jsonCommentList.length(); i++) {
					commentList[i] = new CommentDTO(jsonCommentList.getInt(i));
				}

				result.add(new PostVO(id, author_id, title, body, published, commentList));
			}
			return result;
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				return null;
			}
			throw new RuntimeException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public List<PublicPostDTO> getPosts(String userId, boolean hasPermission) {
		// TODO : 구현 (권한 확인해서 권한이 있는 정보만 DB에 요청해서)
		return null;
	}

	@Override
	public boolean updatePost(String postId, String title, String body) {

		Connection conn = null;
		Statement stat = null;

		SQLConnection con = sqlManager.newConnection();
		conn = con.conn();
		stat = con.stat();

		try {
			stat.execute("USE aant;");
			int count = stat.executeUpdate(String.format("UPDATE Posts SET title='%s', body='%s' WHERE id='%s';", title, body, postId));
			if (count < 1) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				// TODO : SQL Exception Handling (Don't throw error) (SQLIntegrityConstraintViolationException)
				return false;
			}
			throw new RuntimeException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 *
	 * @param post Post article data. id of the post instance will be ignored.
	 * @return
	 */
	@Override
	public String createPost(PostVO post) {
		Connection conn = null;
		Statement stat = null;

		SQLConnection con = sqlManager.newConnection();
		conn = con.conn();
		stat = con.stat();

		String id = UUID.randomUUID().toString();

		try {
			// TODO : id 생성 방식 바꾸기
			stat.execute("USE aant");
			int count = stat.executeUpdate("INSERT INTO Posts (id, author_id, title, body, published, comments, comment_list) " +
					"values" + String.format(" (\"%s\", \"%s\", \"%s\", \"%s\", %s,\"[]\", \"[]\")", id, post.getAuthorId(), post.getTitle(), post.getBody(), post.isPublished() ? "1" : "0"));

//			stat.executeQuery("SELECT (id) FROM Article WHERE Article.")
			if (count == 0) {
				return null;
			}
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				// TODO : SQL Exception Handling (Don't throw error) (SQLIntegrityConstraintViolationException)
			}
			throw new RuntimeException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return id;
	}

	@Override
	public boolean deleteArticle(String id) {
		// TODO : deleteArticle() 구현
		return false;
	}
}
