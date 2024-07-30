package com.sh4316.aant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

@SpringBootTest
class TukAAntBackendApplicationTests {

	@Test
	void contextLoads() throws SQLException, ClassNotFoundException {
//		String url = "jdbc:mysql://localhost:3306";
//		String username = "root";
//		String password = "";
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		Connection conn = DriverManager.getConnection(url, username, password);
//
//		conn.prepareStatement("CREATE TABLE article {};")
	}

}
