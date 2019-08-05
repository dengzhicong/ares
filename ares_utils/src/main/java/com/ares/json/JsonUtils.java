package com.ares.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
public class JsonUtils {

	public static void main(String[] args) {
//		User user = new User();
//		user.setAge(12);
//		user.setMan(true);
//		user.setName("Hel");
//
//		User user2 = new User();
//		user2.setAge(2);
//		user2.setMan(false);
//		user2.setName("Ds");
		
//		List<User> users = new ArrayList<User>();
//		users.add(user);
//		users.add(user2);
//
//		String jsonString = toJSONString(users);
//		System.out.println(jsonString);
//
//		List<User> parseArray = parseArray(jsonString, User.class);
//
//		System.out.println(toJSONString(parseArray));
//		
//		int[] a= {1,2,3};
//		jsonString = toJSONString(a);
//		System.out.println(jsonString);
//		
//		List<Integer> parseArray2 = parseArray(jsonString, Integer.class);
//		System.out.println(toJSONString(parseArray2));
		
		String str="[{\"key\":4,\"value\":4},{\"key\":3,\"value\":7},{\"key\":2,\"value\":12},{\"key\":1,\"value\":25},{\"key\":5,\"value\":2},{\"key\":6,\"value\":2},{\"key\":7,\"value\":8},{\"key\":8,\"value\":11},{\"key\":9,\"value\":0},{\"key\":10,\"value\":0}]\r\n" + 
				"";
	}

	/**
	 * 把对象转换为JSON字符串
	 */
	public static String toJSONString(Object object) {
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
	public static <T> Object parseObject(String json, Class<T> clazz) {
		T result = null;
		try {
			result = JSON.parseObject(json, clazz);
		} catch (Exception e) {
			LogUtil.error("json转换异常,json:" + json + "  class :" + clazz.getName(), e);
		}
		return result;
	}

	public static <T> List<T> parseArray(String json, Class<T> clazz) {
		List<T> list = null;
		try {
			list = JSONArray.parseArray(json, clazz);
		} catch (Exception e) {
			LogUtil.error("json转换异常,json:" + json + "  class :" + clazz.getName(), e);
		}
		return list;
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
