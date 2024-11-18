package com.sh4316.aant.post.utils;

import com.sh4316.aant.post.vo.RawLoginType;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class SigninTypeAdapter {
	public List<SignInType> getAllLoginTypes(final RawLoginType rawLoginType) {
		LinkedList<SignInType> result = new LinkedList<>();
		for (SignInType type : SignInType.values()) {
			if ((rawLoginType.value() & (1L << (type.getValue()-1))) > 0) {
				result.add(type);
			}
		}
		return result;
	}
	public boolean hasLoginType(final SignInType signInType, final RawLoginType rawLoginType) {
		return ((rawLoginType.value() & (1L << (signInType.getValue()-1))) > 0);
	}
	public RawLoginType toRawValue(SignInType signInType) {
		return new RawLoginType(1L << (signInType.getValue()-1));
	}
	public RawLoginType toRawValue(RawLoginType rawLoginType, SignInType signInType) {
		return new RawLoginType(rawLoginType.value() | (1L << (signInType.getValue()-1)));
	}
}
