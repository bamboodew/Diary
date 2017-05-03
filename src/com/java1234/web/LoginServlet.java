package com.java1234.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java1234.dao.UserDao;
import com.java1234.model.User;
import com.java1234.util.DbUtil;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	UserDao userDao = new UserDao();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();// 从网页客户端发来的请求建立会话
		String userName = request.getParameter("userName"); // 服务器从网页发来的请求获取用户信息
		String password = request.getParameter("password");

		Connection connection = null;
		try {
			connection = dbUtil.getCon();
			User user = new User(userName, password);
			User currentUser = userDao.login(connection, user);
			if (currentUser == null) {
				System.out.println("error");
				request.setAttribute("user", user); // 在request对象中加入名为"user"的属性（键）并附值为user（值）
				request.setAttribute("error", "用户名或密码错误！"); // 将web客户端的error属性复制
				request.getRequestDispatcher("login.jsp").forward(request, response);

			} else {
				System.out.println("success");
				session.setAttribute("currentUser", currentUser);// 赋值给会话，确定会话的特定登录用户
				response.sendRedirect("main.jsp");// 服务器向客户端发送跳转，到主页
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
