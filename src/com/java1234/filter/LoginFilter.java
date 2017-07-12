package com.java1234.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest; // 父接口向子接口的强制类型转换
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		HttpSession httpSession = httpServletRequest.getSession(); // 通过request获取session
		Object object = httpSession.getAttribute("currentUser"); // 登录的结果
		String path = httpServletRequest.getServletPath();
		System.out.println(path);

		if (path.equals("/login.jsp") || path.equals("/login") || object != null) {
			filterChain.doFilter(servletRequest, servletResponse); // filter内部处理之后，跳转至正常的response
		} else {
			httpServletResponse.sendRedirect("login.jsp");
		}
	}

	@Override
	public void destroy() {

	}

}
