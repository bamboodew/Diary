package com.java1234.util;

import java.sql.Connection;
import java.sql.DriverManager;
/*
 * 连接数据库工具
 */
public class DbUtil {

	private String dbUrl = "jdbc:mysql://localhost:3306/db_diary?useSSL=false";
	private String dbUserName = "root";
	private String dbPassword = "123456";
	private String jdbcName = "com.mysql.jdbc.Driver";

	public Connection getCon() throws Exception {
		Class.forName(jdbcName);// 反射
		Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
		return connection;
	}

	public void closeCon(Connection connection) throws Exception {
		if (connection!=null) {
			connection.close();
		}
	}
	
	public static void main(String[] args) {
		DbUtil dbUtil=new DbUtil();
		try {
			dbUtil.getCon();
			System.out.println("数据库连接成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据库连接失败！");
		}
	}
}
