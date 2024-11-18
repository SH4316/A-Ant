package com.sh4316.aant.post.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashUtility {
	public String hash(String message) {
		return hash(message, "SHA-256");
	}
	public String hash(String message, String algorithm) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] hash;
		md.update(message.getBytes(StandardCharsets.UTF_8));
		hash = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : hash) {
			sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
}
