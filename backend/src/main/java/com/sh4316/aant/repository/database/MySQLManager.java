package com.sh4316.aant.repository.database;

import jakarta.annotation.PostConstruct;
import org.checkerframework.framework.qual.PostconditionAnnotation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@Component
public class MySQLManager implements SQLManager {

//	private static MySQLManager instance = null;
//
//	public MySQLManager() {
//		if (instance == null) {
//			instance = this;
//		}
//	}

	@Value("${spring.datasource.driver-class-name}")
	private String databaseDriver;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;

	@PostConstruct
	public void printInfo() {
		System.out.println("DB Driver : " + databaseDriver);
		System.out.println("URL : " + url);
		System.out.println("USER : " + username);

		SQLConnection connection = null;
		try {
			connection = newConnection();
			connection.stat().execute("CREATE DATABASE IF NOT EXISTS aant;");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				connection.conn().close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}


	// TODO : Connection Pool 제작

	@Override
	public SQLConnection newConnection() {
		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
			Class.forName(databaseDriver);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		Connection conn = null;
		Statement stat = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
			stat = conn.createStatement();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return new SQLConnection(conn, stat);
	}
}
