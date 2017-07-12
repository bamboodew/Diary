package com.java1234.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.java1234.model.DiaryType;

/**
 * 查询类别下的日志数量
 *
 * @author Administrator
 *
 */
public class DiaryTypeDao {

	public List<DiaryType> diaryTypeCountList(Connection con) throws Exception {
		List<DiaryType> diaryTypeCountList = new ArrayList<DiaryType>();
		// 某个类别下没有日志，内连接是查不到的。而要求即使没有，也要显示0。因此采用右连接。
		String sql = "select  diaryTypeId,typeName,Count(diaryId) as diaryCount from t_diary Right JOIN t_diaryType ON t_diary.typeId=t_diaryType.diaryTypeId Group by typeName;";
		PreparedStatement pstmt = con.prepareStatement(sql);

		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			DiaryType diaryType = new DiaryType(); // 实例化一个diaryType对象
			diaryType.setDiaryTypeId(rs.getInt("diaryTypeId"));
			diaryType.setTypeName(rs.getString("typeName"));
			diaryType.setDiaryCount(rs.getInt("diaryCount"));
			diaryTypeCountList.add(diaryType); // 每次遍历将diaryType添加进去
		}
		return diaryTypeCountList;
	}

}
