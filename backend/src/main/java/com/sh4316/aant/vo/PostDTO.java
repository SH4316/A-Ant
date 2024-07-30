package com.sh4316.aant.vo;

import com.sh4316.aant.vo.dto.CommentDTO;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

//@RequiredArgsConstructor
public class PostDTO implements JSONString {
	@Getter private final int id;
	@Getter private final String authorId;
	@Getter private final String title;
	@Getter private final String body;
	@Getter private final CommentDTO[] comments;

	public PostDTO(int id, String authorId, String title, String body, CommentDTO[] comments) {
		this.id = id;
		this.authorId = authorId;
		this.title = title;
		this.body = body;
		this.comments = comments;
	}

	@Override
	public String toJSONString() {
		JSONArray jsonComments = new JSONArray();
		for (CommentDTO c : comments) {
			jsonComments.put(c.toJSONString());
		}
		return new JSONObject()
				.put("id", id)
				.put("author_id", authorId)
				.put("title", title)
				.put("body", body)
				.put("comments", jsonComments)
				.toString();
	}

	@Override
	public String toString() {
		return toJSONString();
	}
}
