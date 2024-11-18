package com.sh4316.aant.userauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailAuthDto {

	@JsonProperty("email")
	public String email;
	@JsonProperty("password")
	public String password;

	public EmailAuthDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
