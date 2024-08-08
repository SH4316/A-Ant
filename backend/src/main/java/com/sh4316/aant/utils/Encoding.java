package com.sh4316.aant.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Encoding {
	/**
	 * @param str
	 * @return SHA256 + BASE64 Encoding.
	 */
	public static String encoding(String str) {
		return Base64.getEncoder().encodeToString(Hashing.sha256().hashString(str, StandardCharsets.UTF_8).asBytes());
	}
	public static String encodeBase64(String str) {
		return Base64.getEncoder().encodeToString(str.getBytes());
	}
	public static String decodeBase64(String str) {
		return new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
	}
}
