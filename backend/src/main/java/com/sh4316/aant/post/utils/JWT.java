package com.sh4316.aant.post.utils;

import org.json.JSONObject;

@Deprecated
public class JWT {
	private String iss = "popu.kr"; // TODO : 하드코딩 x
	private JSONObject header;
	private JSONObject payload;

	public JWT() {
		header = new JSONObject()
				.put("typ", "JWT")
				.put("alg", "SHA256");
		payload = new JSONObject()
				.put("iss", iss)
				.put("exp", String.valueOf(System.currentTimeMillis()));

	}

	public static JWT create() {
		return new JWT();
	}

	public JWT put(String key, String value) {
		payload.put(key, value);
		return this;
	}
	public JWT hashing(String algorithm) {
		header.put("alg", algorithm);
		return this;
	}

	public String build() {
		String header = Encoding.encodeBase64(this.header.toString());
		String payload = Encoding.encodeBase64(this.payload.toString());
		// TODO : 암호화 알고리즘 다른 것도 사용할 수 있도록 하기
		// TODO : 생성된 jwt가 안전한지 확인
		return header + "."
				+ payload + "."
				+ Encoding.encoding(header + "." + payload + "." + "jae5i6uf7h56p5ie142a");
		// TODO : 키 하드코딩 x
	}
}
