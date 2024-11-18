package com.sh4316.aant.post.vo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

public class CommentDTO implements org.json.JSONString{
	@Getter private final int id;
	@Getter private final String body;
	@Getter private final boolean loaded;

	public CommentDTO(int id, String body, boolean loaded) {
		this.id = id;
		this.body = body;
		this.loaded = loaded;
	}
	public CommentDTO(int id, String body) {
		this.id = id;
		this.body = body;
		this.loaded = true;
	}

	public CommentDTO(int id) {
		this.id = id;
		this.body = "";
		this.loaded = false;
	}

	@Override
	public String toJSONString() {
		return new JSONObject()
				.put("id", id)
				.put("body", body)
				.put("loaded", loaded)
				.toString();
	}

	@Override
	public String toString() {
		return new JSONObject()
				.put("id", id)
				.put("body", body)
				.put("loaded", loaded)
				.toString();
	}
}
