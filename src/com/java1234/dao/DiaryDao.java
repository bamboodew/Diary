package com.java1234.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.java1234.model.Diary;
import com.java1234.model.PageBean;
import com.java1234.util.DateUtil;
import com.java1234.util.StringUtil;

public class DiaryDao {

	/*
	 * 返回【日志列表】
	 */
	public List<Diary> diaryList(Connection con,PageBean pageBean,Diary s_diary) throws Exception {
		List<Diary> diaryList = new ArrayList<Diary>();
		StringBuffer sb = new StringBuffer("select * from t_diary t1,t_diaryType t2 where t1.typeId=t2.diaryTypeId");

		if (StringUtil.isNotEmpty(s_diary.getTitle())) {
			sb.append(" and t1.title like '%"+s_diary.getTitle()+"%'"); // 模糊查询
		}

		if (s_diary.getTypeId()!=-1) {
			sb.append(" and t1.typeId="+s_diary.getTypeId());
		}
		if (StringUtil.isNotEmpty(s_diary.getReleaseDateStr())) {
			sb.append(" and DATE_FORMAT(t1.releaseDate,'%Y年%m月')='"+s_diary.getReleaseDateStr()+"'");
		}
		sb.append(" order by t1.releaseDate desc"); // sql语句不要写错，可以现在mysql中验证一下

		// 分页：获取一页的数据
		if (pageBean != null) {
			sb.append(" limit " + pageBean.getStart() + "," + pageBean.getPageSize());
			// limit前后空一格；select * from tablename limit 2,4 —— 取出第3条至第6条，4条记录
		}

		PreparedStatement preparedStatement = con.prepareStatement(sb.toString());
		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			Diary diary = new Diary();
			diary.setDiaryId(resultSet.getInt("diaryId"));
			diary.setTitle(resultSet.getString("title"));
			diary.setContent(resultSet.getString("content"));
			diary.setReleaseDate(DateUtil.formatString(resultSet.getString("releaseDate"), "yyyy-MM-dd HH:mm:ss"));
			diaryList.add(diary);
		}
		return diaryList;
	}

	/*
	 * 返回【日志总数量】
	 */
	public int diaryCount(Connection con,Diary s_diary) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_diary t1,t_diaryType t2 where t1.typeId=t2.diaryTypeId");
		if (StringUtil.isNotEmpty(s_diary.getTitle())) {
			sb.append(" and t1.title like '%"+s_diary.getTitle()+"%'"); // 显示按日志名称模糊搜索的结果的页数
		}
		if (s_diary.getTypeId()!=-1) {
			sb.append(" and t1.typeId="+s_diary.getTypeId()); // 显示按日志类型的页数
		}
		if (StringUtil.isNotEmpty(s_diary.getReleaseDateStr())) {
			sb.append(" and DATE_FORMAT(t1.releaseDate,'%Y年%m月')='"+s_diary.getReleaseDateStr()+"'");
			// 显示按日期的页数
		}
		PreparedStatement preparedStatement = con.prepareStatement(sb.toString());
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getInt("total"); // 字段为“total”的值
		}else{
			return 0;
		}
	}

	/*
	 * 【按日期返回日志列表】
	 */
	public List<Diary> diaryCountList(Connection con) throws Exception{
		List<Diary> diaryCountList=new ArrayList<Diary>();
		String sql="select date_format(releaseDate,'%Y年%m月') as releaseDateStr,count(*) as diaryCount from t_diary group by date_format(releaseDate,'%Y年%m月') order by date_format(releaseDate,'%Y年%m月') desc";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Diary diary = new Diary(); // 实例化一个diaryType对象
			diary.setReleaseDateStr(rs.getString("releaseDateStr"));
			diary.setDiaryCount(rs.getInt("diaryCount"));
			diaryCountList.add(diary); // 每次遍历将diaryType添加进去
		}
		return diaryCountList;

	}
}
