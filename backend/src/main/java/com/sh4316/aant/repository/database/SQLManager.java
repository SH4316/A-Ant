package com.sh4316.aant.repository.database;

import org.springframework.stereotype.Component;

@Component
public interface SQLManager {
	public SQLConnection newConnection();
}
