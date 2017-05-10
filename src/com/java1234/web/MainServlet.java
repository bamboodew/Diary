package com.java1234.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java1234.dao.DiaryDao;
import com.java1234.model.Diary;
import com.java1234.model.PageBean;
import com.java1234.util.DbUtil;
import com.java1234.util.PropertiesUtil;
import com.java1234.util.StringUtil;

public class MainServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	DiaryDao diaryDao = new DiaryDao();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8"); // 避免乱码
		String page = request.getParameter("page"); // 前台传入当前页“page”
		if (StringUtil.isEmpty(page)) {
			page = "1";
		}
		Connection connection = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));

		try {
			connection = dbUtil.getCon();

			List<Diary> diaryList = diaryDao.diaryList(connection, pageBean); // 通过sql语句获取1页数据
			int total=diaryDao.diaryCount(connection); // 通过sql语句获取日志总数量
			String pageCode = this.genPagation(total, Integer.parseInt(page),
					Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			 // 通过genPagation方法获取分页样式的代码

			request.setAttribute("diaryList", diaryList); // diaryList.jsp中EL表达式：键值对——赋值
			request.setAttribute("pageCode", pageCode); // diaryList.jsp中EL表达式：键值对——赋值
			request.setAttribute("mainPage", "diary/diaryList.jsp"); // 链接————对mainPage赋值为diaryList.jsp
			request.getRequestDispatcher("mainTemp.jsp").forward(request, response); // 内部转发【跳转】至mainTemp.jsp
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 后台拼凑出【分页样式】组件的html代码
	 *
	 * @param totalNum
	 *            —— 日志总条数
	 * @param currentPage
	 *            —— 当前的页码
	 * @param pageSize
	 *            —— 每页的日志数
	 * @return
	 */
	@SuppressWarnings("unused")
	private String genPagation(int totalNum, int currentPage, int pageSize) {
		int totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1; // 三目运算符，判断计算总页数。
		StringBuffer pageCode = new StringBuffer(); // 字符串章节
		pageCode.append("<li><a href='main?page=1'>首页</a></li>");
		// Appends（附加/添加） the specified string to this character sequence.

		if (currentPage == 1) {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
			// 如果是第一页：不增加链接。
		} else {
			pageCode.append("<li><a href='main?page=" + (currentPage - 1) + "'>上一页</a></li>");
		}

		for (int i = currentPage - 2; i <= currentPage + 2; i++) {
			if (i < 1 || i > totalPage) {
				continue;
			}
			if(i==currentPage){
				pageCode.append("<li class='active'><a href='#'>"+i+"</a></li>"); // 显示当前页的页码
			}else {
				pageCode.append("<li><a href='main?page=" + i + "'>"+i+"</a></li>");
			}
		}

		if (currentPage == totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
			// 如果是尾页：不增加链接。
		} else {
			pageCode.append("<li><a href='main?page=" + (currentPage + 1) + "'>下一页</a></li>");
		}
		pageCode.append("<li><a href='main?page="+totalPage+"'>尾页</a></li>");
		return pageCode.toString();
	}

}
