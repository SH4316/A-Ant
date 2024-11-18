package com.sh4316.aant.post.vo.dto;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

public class PublicPostDTO implements JSONString {
	@Getter private final String id;
	@Getter private final String title;
	@Getter private final String body;
	@Getter private final CommentDTO[] comments;

	public PublicPostDTO(String id, String title, String body, CommentDTO[] comments) {
		this.id = id;
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
