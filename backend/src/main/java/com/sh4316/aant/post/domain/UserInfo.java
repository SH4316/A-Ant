package com.sh4316.aant.post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@Table(name = "PublicUserInfo")
public class UserInfo {
	@Column(name = "email")
	private String email;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "description")
	private String description;

	@Column(name = "externalLink")
	private String externalLink;
}
