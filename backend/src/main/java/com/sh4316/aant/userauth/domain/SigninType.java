package com.sh4316.aant.userauth.domain;

import com.sh4316.aant.post.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

@Getter
@Setter
@Entity
public class SigninType {
	@Id
	@Column(name = "user_id")
	private Long userId;

	@OneToOne
	@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(name = "type", nullable = false)
	private Long type;
}
