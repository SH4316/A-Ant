package com.sh4316.aant.repository;

import com.sh4316.aant.vo.PostDTO;
import com.sh4316.aant.vo.dto.CommentDTO;
import com.sh4316.aant.repository.database.SQLConnection;
import com.sh4316.aant.repository.database.SQLManager;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;

public class PostRepositoryImpl implements PostRepository {

	private SQLManager sqlManager;

	public PostRepositoryImpl(@Autowired SQLManager sqlManager) {
		this.sqlManager = sqlManager;
	}

	@PostConstruct
	public void init() {

		Connection conn = null;
		Statement stat = null;

		SQLConnection con = sqlManager.newConnection();
		conn = con.conn();
		stat = con.stat();


		try {
			stat.execute("USE aant");
			stat.execute("CREATE TABLE IF NOT EXISTS Posts (" +
					"id INT AUTO_INCREMENT PRIMARY KEY," +
					"author_id VARCHAR(45)," +
					"title TEXT NOT NULL," +
					"body TEXT NOT NULL," +
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
	public PostDTO getPost(int aid) {
		Connection conn = null;
		Statement stat = null;

		SQLConnection con = sqlManager.newConnection();
		conn = con.conn();
		stat = con.stat();

		ResultSet resultSet = null;
		try {
			stat.execute("USE aant");
			resultSet = stat.executeQuery("SELECT * FROM Posts WHERE Posts.id = \"" + aid + "\";");

			if (!resultSet.next()) {
				conn.close();
				return null;
			}
			int id = resultSet.getInt(1);
			String author_id = resultSet.getString(2);
			String title = resultSet.getString(3);
			String body = resultSet.getString(4);
			String comments = resultSet.getString(5);
			String comment_list = resultSet.getString(6);
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
			return new PostDTO(id, author_id, title, body, commentList);
		} catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			throw new RuntimeException(e);
		}
	}


	@Override
	public boolean updatePost(int postId, String title, String body) {
		// TODO : updateArticle() 구현

		return false;
	}

	@Override
	public boolean createPost(PostDTO post) {
		Connection conn = null;
		Statement stat = null;

		SQLConnection con = sqlManager.newConnection();
		conn = con.conn();
		stat = con.stat();

		try {
			// TODO : id 생성 방식 바꾸기
			stat.execute("USE aant");
			stat.executeUpdate("INSERT INTO Posts (author_id, title, body, comments, comment_list) " +
					"values (\"" + post.getAuthorId() + "\", \"" + post.getTitle() + "\", \"" + post.getBody() + "\", \"[]\", \"[]\")");

//			stat.executeQuery("SELECT (id) FROM Article WHERE Article.")
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				// TODO : SQL Exception Handling (Don't throw error) (SQLIntegrityConstraintViolationException)
			}
			throw new RuntimeException(e);
		}
		// TODO : post id(int) 리턴하게 만들기
		return true;
	}

	@Override
	public boolean deleteArticle(int id) {
		// TODO : deleteArticle() 구현
		return false;
	}
}
