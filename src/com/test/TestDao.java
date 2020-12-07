package com.test;

import com.bean.TypeBean;
import com.bean.UserBean;
import com.dao.TypeDao;
import com.dao.UserDao;

import java.sql.SQLException;
import java.util.List;

public class TestDao {

	public static void main(String[] args) throws SQLException {
		TypeDao typeDao=new TypeDao();
		TypeBean type = typeDao.getTypeById(1);
		System.out.println(type.getTypeName());

		UserDao userDao=new UserDao();
		List<UserBean> userList = userDao.getUserList(0, 10, null);
		for(UserBean userBean:userList){
			System.out.println(userBean);
		}
		int userCount = userDao.getUserCount(null);
		System.out.println(userCount);
	}

}
