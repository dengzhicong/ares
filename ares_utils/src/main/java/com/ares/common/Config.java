package com.ares.common;

import com.ares.log.LogUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件封装类
 * 
 * @author laofan 根据配置文件 config.properties来初始化服务器的配置信息
 * 
 *
 */
public class Config {

	private static Properties properties = null;
	private static String mainPath = null;
	private static String filePath = null;

	public static boolean initConfig(String basePath, String path) {
		if (basePath == null) {
			return false;
		}
		if (!basePath.endsWith("/")) {
			basePath = basePath + "/";
		}
		mainPath = basePath;
		System.err.println("mainPath=" + mainPath);
		if (path == null || path.equals("")) {
			return false;
		}
		if (properties == null) {
			filePath = mainPath + path;
			System.err.println("filePath=" + filePath);
			return loadProperties(filePath);
		}
		return true;
	}

	private static boolean loadProperties(String path) {
		try {
			properties = new Properties();
			InputStream inputStream = new BufferedInputStream(new FileInputStream(path));
			properties.load(inputStream);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取值
	 * 
	 * @param key
	 * @return
	 */
	public static String getPath(String key) {
		if (properties == null) {
			return null;
		}
		String path = properties.getProperty(key);
		if (path == null || path.trim().length() == 0) {
			return "";
		}
		if (mainPath != null && mainPath.trim().length() > 0) {
			if (path.startsWith("/") || mainPath.endsWith("/")) {
				path = mainPath + path;
			} else {
				path = mainPath + "/" + path;
			}
		}
		return path;
	}

	/**
	 * 读取值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		if (properties == null) {
			return null;
		}
		return properties.getProperty(key);
	}

	/**
	 * 读取值
	 * 
	 * @param key
	 * @return
	 */
	public static int getInt(String key) {
		if (properties == null) {
			return 0;
		}
		String value = properties.getProperty(key);
		if (value == null) {
			return 0;
		}
		if (SplitUtil.isNumeric(value)) {
			return Integer.parseInt(value);
		}
		return 0;
	}

	/**
	 * 读取值
	 * 
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key) {
		if (properties == null) {
			return false;
		}
		String value = properties.getProperty(key);
		try {
			Boolean isTrue = Boolean.parseBoolean(value);
			return isTrue;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 读取值
	 * 
	 * @param key
	 * @return
	 */
	public static double getDouble(String key) {
		if (properties == null) {
			return 0;
		}
		String value = properties.getProperty(key);
		if (value == null) {
			return 0;
		}
		return Double.parseDouble(value);
	}

	/**
	 * 更改properties中的值，并更新config.properties文件
	 * 
	 * @param key
	 * @param value
	 */
	public static void setValue(String key, Object value) {
		if (properties == null) {
			return;
		}

		try {
			properties.setProperty(key, String.valueOf(value));
			FileOutputStream fos = new FileOutputStream(filePath);
			properties.store(fos, null);
			fos.close();
		} catch (IOException e) {
			LogUtil.error("更新properties文件失败", e.fillInStackTrace());
		}
	}
}
