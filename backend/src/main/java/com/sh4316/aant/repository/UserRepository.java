package com.sh4316.aant.repository;

import com.sh4316.aant.vo.dto.UserDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

	public UserDTO getUser(String email);
	public UserDTO getUserById(String id);
	public String getUserId(String email);
	public String getPassword(String email);
	public String getPasswordById(String id);
	public String getUserType(int userType);
	public int getUserType(String userType);

	/**
	 * @param userType Use 'getUserType(String): int'
	 * @return Return user id;
	 */
	public String registerUser(String email, String password, int userType);

	/**
	 * @return Return false if there not exist account.
	 */
	public boolean changePassword(String id, String password);

	/**
	 * @return Return false if there not exist account.
	 */
	public boolean deleteUser(String id);
}
