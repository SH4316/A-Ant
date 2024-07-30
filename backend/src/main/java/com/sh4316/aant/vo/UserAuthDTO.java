package com.sh4316.aant.vo;


import com.fasterxml.jackson.annotation.JsonProperty;

public record UserAuthDTO(@JsonProperty("email") String email, @JsonProperty("pw") String password) {
}
