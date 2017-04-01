package com.Twilighce.service;
import com.Twilighce.dao.UserDAO;
import com.Twilighce.model.User;



public class UserService {
	private UserDAO userDAO;  
	public void add(User user) {
		userDAO.save(user);
	}
	public UserDAO getUserDAO() {
		return userDAO;
	}
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
