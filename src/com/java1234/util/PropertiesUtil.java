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
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (String) prop.get(key);
	}
}
