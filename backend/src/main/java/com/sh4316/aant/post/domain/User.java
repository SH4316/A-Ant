package com.sh4316.aant.post.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "UserBasicInfo")
@SecondaryTable(
		name = "PublicUserInfo",
		pkJoinColumns = @PrimaryKeyJoinColumn(
				name = "userId",
				referencedColumnName = "id"
		)
)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Embedded
	private UserInfo publicInfo;
}
