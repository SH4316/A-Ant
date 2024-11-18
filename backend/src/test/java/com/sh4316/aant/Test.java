package com.sh4316.aant;


import com.google.common.hash.Hashing;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class Test {
	public static void main(String[] args) {
//		String str = "a;w49071249039uh4gruihosoiheso";
		String str = "a;w4907hesok";

//		byte[] encodedByte = Base64.getEncoder().encode(str.getBytes());
//		String encoded = String.valueOf(encodedByte);
//		String decoded = String.valueOf(Base64.getDecoder().decode(encodedByte));


//		System.out.println(str);
//		System.out.println(str.getBytes());
//		System.out.println();
//		System.out.println(Base64.getEncoder().encodeToString(str.getBytes()));
//		System.out.println(Base64.getEncoder().encode(str.getBytes()));
////		System.out.println(new String(encodedByte, StandardCharsets.UTF_8));
//		System.out.println();
//		System.out.println(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(str.getBytes())));
//		System.out.println(new String(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(str.getBytes())), StandardCharsets.UTF_8));


		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] hash;
		md.update(str.getBytes(StandardCharsets.UTF_8));
		hash = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : hash) {
			sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
		}
		System.out.println(sb.toString());
	}
}
// 392430352313499276486493493385480274344492291430343349299323308276504257310321509296342315495481
// 310434446274264450274424448304478465384289487301485413297366375334439509502371474423378494511436
// 64e4223e25 8400222f80 119d1b639e 7e493a3265 9a1214c : 48
// 36b2be1208 c212a8c030 ded18021e7 2de59d296e 774eb7fdf6 73daa77aee ffb4 : 64

