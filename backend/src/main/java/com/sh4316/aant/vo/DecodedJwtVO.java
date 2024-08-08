package com.sh4316.aant.vo;

import lombok.Getter;

public record DecodedJwtVO(@Getter String header, @Getter String payload) {
}
