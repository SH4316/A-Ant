package com.sh4316.aant.post.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
//	private final SigninTypeAdapter loginTypeAdapter;
//	private final DataSource dataSource;
//	private final HashUtility hashUtility;
//
//	@Autowired
//	public LoginService(LoginDao loginDao, UserDao userDao, SigninTypeAdapter loginTypeAdapter, DataSource dataSource, HashUtility hashUtility) {
//		this.loginDao = loginDao;
//		this.userDao = userDao;
//		this.loginTypeAdapter = loginTypeAdapter;
//		this.dataSource = dataSource;
//		this.hashUtility = hashUtility;
//	}
//
//
//	@Transactional
//	public List<SignInType> getPossibleLoginType(User user) {
//		return loginTypeAdapter.getAllLoginTypes(loginDao.getLoginTypes(user));
//	}
//
//	public boolean authenticate(String email, String password) {
//		// TODO : check email format
////		TransactionSynchronizationManager.initSynchronization();
//		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
//		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
//
//		try {
//			String emailFront = email.split("@")[0];
//			String emailDomain = email.split("@")[1];
//			Login login = loginDao.getLogin(SignInType.EMAIL, emailFront, emailDomain);
//			if (login == null || login.getClass() == EmailLogin.class)
//				return false;
//			((EmailLogin) login).authenticate(hashUtility, email, password);
//
//			transactionManager.commit(status);
//		} catch (Exception e) {
//			transactionManager.rollback(status);
//			return false;
//		}
//		return true;
//	}

}
