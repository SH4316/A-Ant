package com.sh4316.aant.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
}
