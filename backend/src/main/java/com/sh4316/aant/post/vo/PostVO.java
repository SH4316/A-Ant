package com.sh4316.aant.post.vo;

import com.sh4316.aant.post.vo.dto.CommentDTO;
import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

//@RequiredArgsConstructor
public class PostVO implements JSONString {
	@Getter private final String id;
	@Getter private final String authorId;
	@Getter private final String title;
	@Getter private final String body;
	@Getter private final boolean published;
	@Getter private final CommentDTO[] comments;

	public PostVO(String id, String authorId, String title, String body, boolean published, CommentDTO[] comments) {
		this.id = id;
		this.authorId = authorId;
		this.title = title;
		this.body = body;
		this.published = published;
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
				.put("published", published)
				.put("comments", jsonComments)
				.toString();
	}

	@Override
	public String toString() {
		return toJSONString();
	}
}
