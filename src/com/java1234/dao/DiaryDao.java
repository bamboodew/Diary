package com.java1234.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.java1234.model.Diary;
import com.java1234.model.PageBean;
import com.java1234.util.DateUtil;

public class DiaryDao {

	/*
	 * 返回【日志列表】
	 */
	public List<Diary> diaryList(Connection con,PageBean pageBean) throws Exception {
		List<Diary> diaryList = new ArrayList<Diary>();
		StringBuffer sb = new StringBuffer("select * from t_diary t1,t_diaryType t2 where t1.typeId=t2.diaryTypeId");
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
	public int diaryCount(Connection con) throws Exception{
		StringBuffer sb = new StringBuffer("select count(*) as total from t_diary t1,t_diaryType t2 where t1.typeId=t2.diaryTypeId");
		PreparedStatement preparedStatement = con.prepareStatement(sb.toString());
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getInt("total"); // 字段为“total”的值
		}else{
			return 0;
		}

	}
}
