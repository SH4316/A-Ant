package com.sh4316.aant.userauth.domain;

import com.sh4316.aant.post.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "EmailLoginInfo")
public class EmailLogin {
	@Id
	@Column(name = "user_id")
	private Long userId;

	@OneToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	private String email;

	private String password;

	public EmailLogin() {
	}
}
