package com.ares.json;

import java.util.Collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ares.log.LogUtil;

/**
 * JSON转换工具
 * 
 * @author Noah
 *
 */
public class JSONUtil {

	/**
	 * 把对象转换为JSON字符串
	 */
	public static String getJSONString(Object object) {
		String jsonString = null;
		try {
			if (object != null) {
				if (object instanceof Collection || object instanceof Object[]) {
					// 禁止“$ref”的循环引用打印出来
					jsonString = JSONArray.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
				} else {
					// 禁止“$ref”的循环引用打印出来
					jsonString = JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
				}
			}
		} catch (Exception e) {
			LogUtil.error("json转换异常", e);
		}
		return jsonString == null ? "{}" : jsonString;
	}

	/**
	 * 从一个JSON 对象字符格式中得到一个java对象
	 * 
	 * @param <T>
	 */
	public static <T> Object parseObject(String jsonString, Class<T> clazz) {
		T result = null;
		try {
			result = JSON.parseObject(jsonString, clazz);
		} catch (Exception e) {
			LogUtil.error("json转换异常,json:" + jsonString + "  class :" + clazz.getName(), e);
		}
		return result;
	}

	public static String getValue(String json, String filed) {
		try {
			if (json == null) {
				return null;
			}
			JSONObject jobj = JSON.parseObject(json);
			if (jobj.containsKey(filed)) {
				return jobj.getString(filed);
			}
			return null;
		} catch (Exception e) {
			LogUtil.warn("JSON getValue error,json:" + json + " filed" + filed, e);
			return null;
		}
	}

}
