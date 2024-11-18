package com.sh4316.aant.post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Poster {
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "poster_name", nullable = false)
	private String name;
}
