package com.java1234.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.java1234.dao.DiaryDao;
import com.java1234.dao.DiaryTypeDao;
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
	DiaryTypeDao diaryTypeDao = new DiaryTypeDao();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		HttpSession session = request.getSession(); // 操作其他位置，页面一直显示；获取session
		String s_typeId = request.getParameter("s_typeId");
		String s_releaseDateStr = request.getParameter("s_releaseDateStr");
		String s_title = request.getParameter("s_title");
		String all = request.getParameter("all");

		String page = request.getParameter("page");
		Diary diary = new Diary();
		if ("true".equals(all)) {
			if (StringUtil.isNotEmpty(s_title)) {
				diary.setTitle(s_title);
			}
			session.removeAttribute("s_releaseDateStr");
			session.removeAttribute("s_typeId");
			session.setAttribute("s_title", s_title);
		} else {
			if (StringUtil.isNotEmpty(s_typeId)) {
				diary.setTypeId(Integer.parseInt(s_typeId));
				session.setAttribute("s_typeId", s_typeId);
				session.removeAttribute("s_releaseDateStr"); // 查询条件相互独立开
				session.removeAttribute("s_title"); // 查询条件相互独立开
			}
			if (StringUtil.isNotEmpty(s_releaseDateStr)) {
				// s_releaseDateStr = new
				// String(s_releaseDateStr.getBytes("ISO-8859-1"),"UTF-8");
				// 更改日期（字符串）的编码方式，否则有可能取不到值
				diary.setReleaseDateStr(s_releaseDateStr);
				session.setAttribute("s_releaseDateStr", s_releaseDateStr);
				session.removeAttribute("s_typeId"); // 查询条件相互独立开
				session.removeAttribute("s_title");
			}
			/*
			 * 如果页面中的s_typeId为空，但session中有s_typeId对象
			 */
			if (StringUtil.isEmpty(s_typeId)) {
				Object object = session.getAttribute("s_typeId");
				if (object != null) {
					diary.setTypeId(Integer.parseInt(object.toString())); // object需要强制转换为String
				}
			}
			if (StringUtil.isEmpty(s_releaseDateStr)) {
				Object object = session.getAttribute("s_releaseDateStr");
				if (object != null) {
					diary.setReleaseDateStr(object.toString()); // 如果object不为空，就把选定的日期赋给diary
				}
			}
			if (StringUtil.isEmpty(s_title)) {
				Object object = session.getAttribute("s_title");
				if (object != null) {
					diary.setTitle(object.toString()); // 如果object不为空，就把选定的title赋给diary
				}
			}
		}
		if (StringUtil.isEmpty(page)) {
			page = "1";
		}
		Connection con = null;

		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
		try {
			con = dbUtil.getCon();
			List<Diary> diaryList = diaryDao.diaryList(con, pageBean, diary);
			int total = diaryDao.diaryCount(con, diary);
			String pageCode = this.genPagation(total, Integer.parseInt(page),
					Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			request.setAttribute("pageCode", pageCode);
			request.setAttribute("diaryList", diaryList);

			session.setAttribute("diaryTypeCountList", diaryTypeDao.diaryTypeCountList(con));
			session.setAttribute("diaryCountList", diaryDao.diaryCountList(con));

			request.setAttribute("mainPage", "diary/diaryList.jsp");
			request.getRequestDispatcher("mainTemp.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String genPagation(int totalNum, int currentPage, int pageSize) {
		int totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<li><a href='main?page=1'>首页</a></li>");
		if (currentPage == 1) {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		} else {
			pageCode.append("<li><a href='main?page=" + (currentPage - 1) + "'>上一页</a></li>");
		}
		for (int i = currentPage - 2; i <= currentPage + 2; i++) {
			if (i < 1 || i > totalPage) {
				continue;
			}
			if (i == currentPage) {
				pageCode.append("<li class='active'><a href='#'>" + i + "</a></li>");
			} else {
				pageCode.append("<li><a href='main?page=" + i + "'>" + i + "</a></li>");
			}
		}
		if (currentPage == totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		} else {
			pageCode.append("<li><a href='main?page=" + (currentPage + 1) + "'>下一页</a></li>");
		}
		pageCode.append("<li><a href='main?page=" + totalPage + "'>尾页</a></li>");
		return pageCode.toString();
	}

}
