package com.sh4316.aant.repository;

import com.sh4316.aant.vo.dto.UserDTO;
import com.sh4316.aant.repository.database.SQLConnection;
import com.sh4316.aant.repository.database.SQLManager;
import com.sh4316.aant.utils.Encoding;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements UserRepository {

	private SQLManager sqlManager;

	private Map<String, Integer> userTypeMap = new ConcurrentHashMap<>();

	public UserRepositoryImpl(@Autowired SQLManager sqlManager) {
		this.sqlManager = sqlManager;
	}

	@PostConstruct
	public void init() {
		SQLConnection sqlConnection = sqlManager.newConnection();
		Connection conn = sqlConnection.conn();
		Statement stat = sqlConnection.stat();
		try {
			stat.execute("USE aant");
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS Users (" +
					"id VARCHAR(45) PRIMARY KEY," +
					"email VARCHAR(256) NOT NULL," +
					"password VARCHAR(45) NOT NULL," +
					"user_type INT NOT NULL," +
					"FOREIGN KEY (user_type) REFERENCES UserType (id));");

			stat.executeUpdate("CREATE TABLE IF NOT EXISTS UserType (" +
					"id INT AUTO_INCREMENT PRIMARY KEY," +
					"name VARCHAR(45) NOT NULL" +
					");");
			// TODO : UserType 테이블 생성시 기본 UserType 값 추가
//			stat.executeUpdate("INSERT INTO User")

			ResultSet result = stat.executeQuery("SELECT * FROM UserType;");
			while (result.next()) {
				int id = result.getInt(1);
				String name = result.getString(2);
				userTypeMap.put(name, id);
			}

			conn.close();
		} catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserDTO getUser(String email) {
		SQLConnection mySQLConnection = sqlManager.newConnection();
		Connection conn = mySQLConnection.conn();
		Statement stat = mySQLConnection.stat();

		try {
			stat.execute("USE aant;");
			ResultSet result = stat.executeQuery(String.format("SELECT id, email, user_type FROM Users WHERE Users.email='%s';", email));
			if (!result.next()) {
				conn.close();
				return null;
			}
			String id = result.getString(1);
			String emaill = result.getString(2);
			int userType = result.getInt(3);

			conn.close();
			return new UserDTO(id, emaill, userType);
		} catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserDTO getUserById(String id) {
		// TODO : getUserById 구현
		return null;
	}

	@Override
	public @Nullable String getUserId(String email) {
		SQLConnection mySQLConnection = sqlManager.newConnection();
		Connection conn = mySQLConnection.conn();
		Statement stat = mySQLConnection.stat();

		try {
			stat.execute("USE aant;");
			ResultSet result = stat.executeQuery("SELECT (id) FROM Users WHERE Users.email=\"" + email + "\";");
			if (!result.next()) {
				return null;
			}
			return result.getString(1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getPassword(String email) {
		SQLConnection mySQLConnection = sqlManager.newConnection();
		Connection conn = mySQLConnection.conn();
		Statement stat = mySQLConnection.stat();

		try {
			stat.execute("USE aant;");
			ResultSet result = stat.executeQuery(String.format("SELECT (password) FROM Users WHERE Users.email=\"%s\"", email));
			if (!result.next()) {
				conn.close();
				return null;
			}
			String password = result.getString(1);

			conn.close();
			return password;
		} catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getPasswordById(String id) {
		// TODO : 구현
		return null;
	}

	@Override
	public String getUserType(int userType) {
		// TODO : 구현
		return null;
	}

	@Override
	public int getUserType(String userType) {
		Integer result = userTypeMap.get(userType);
		if (result == null) {
			return -1;
		}
		return result;
	}

	@Override
	public String registerUser(String email, String password, int userType) {
		SQLConnection mySQLConnection = sqlManager.newConnection();
		Connection conn = mySQLConnection.conn();
		Statement stat = mySQLConnection.stat();
		String id = null;
		try {
			// TODO : id 생성방식 바꾸기
			id = Encoding.encoding(email);
			stat.execute("USE aant;");
//			ResultSet result = stat.executeQuery("SELECT * FROM \"Users\" WHERE \"Users.email\" = \"" + email + "\"");
//			// TODO : id 중복 확인하기 (또는 중복되지 않는 방법으로 바꾸기)
//			if (result.next()) {
//				conn.close();
//				return null;
//			}
			stat.executeUpdate(String.format("INSERT INTO Users (id, email, password, user_type) VALUES (\"%s\",\"%s\",\"%s\",\"%d\");", id, email, password, userType));

			conn.close();
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				// TODO : primary key 중복일때 에러 대신 로그 찍기
			}
			// TODO : 에러 대신 로그 찍기
			throw new RuntimeException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}

		return id;
	}

	@Override
	public boolean changePassword(String id, String password) {
		// TODO : 구현
		return false;
	}

	@Override
	public boolean deleteUser(String id) {
		SQLConnection mySQLConnection = sqlManager.newConnection();
		Connection conn = mySQLConnection.conn();
		Statement stat = mySQLConnection.stat();
		try {
			int count = stat.executeUpdate(String.format("DELETE FROM Users WHERE Users.id='%s';", id));
			if (count > 0) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
