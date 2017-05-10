package com.java1234.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 通过键获取值
 * @author Administrator
 *
 */
public class PropertiesUtil {

	public static String getValue(String key){
		Properties prop=new Properties();
		InputStream in=new PropertiesUtil().getClass().getResourceAsStream("/diary.properties");
		try {
			prop.load(in); // 从.properties属性文件对应的文件输入流中，加载属性列表到Properties类对象。
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (String) prop.get(key);
	}
}
