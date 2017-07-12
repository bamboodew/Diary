package com.java1234.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.java1234.model.User;
import com.java1234.util.MD5Util;
import com.java1234.util.PropertiesUtil;

/*
 * 判断登录验证
 */
public class UserDao {

	public User login(Connection connection, User user) throws Exception {
		User resultUser = null;// 验证后的用户
		String sql = "select * from t_user where userName=? and password=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, user.getUserName()); //user是用户输入的信息
		pstmt.setString(2, MD5Util.EncoderPwdByMd5(user.getPassword()));
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			resultUser = new User();
			resultUser.setUserId(rs.getInt("userId"));
			resultUser.setUserName(rs.getString("userName"));
			resultUser.setPassword(rs.getString("password"));
			resultUser.setNickName(rs.getString("nickName"));
			resultUser.setImageName(PropertiesUtil.getValue("imageFile")+rs.getString("imageName"));
			resultUser.setMood(rs.getString("mood"));
		}
		return resultUser;
	}
}
