package com.ares.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于key-不同类型value的缓存工具
 * @author admin
 *
 */
public class AttributeMap {
	public static final short SHORT = 0;
	public static final int INT = 0;
	public static final long LONG = 0L;
	public static final double DOUBLE = 0D;
	public static final float FLOAT = 0F;
	public static final String STRING = "";
	public static final boolean BOOLEAN = false;

	public static final Map<Integer, Long> INT_LONG_MAP = new HashMap<>();
	public static final Map<Long, Long> LONG_LONG_MAP = new HashMap<>();
	public static final Map<String, Integer> STRING_INT_MAP = new HashMap<>();
	public static final Map<String, Long> STRING_LONG_MAP = new HashMap<>();
	public static final Map<String, Map<Long, Integer>> STRING_LONG_INT_MAP = new HashMap<>();
	public static final Map<Long, Map<Integer, Long>> LONG_INT_LONG_MAP = new HashMap<>();

	private Map<String, Object> attributes = new ConcurrentHashMap<>();

	/**
	 * 
	 * @param <T>
	 * @param key
	 * @param t 是AttributeMap.INT，AttributeMap.LONG等
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key, T t) {
		return (T) attributes.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> clazz) {
		return (T) attributes.get(key);
	}

	public void set(String key, Object value) {
		attributes.put(key, value);
	}

	public void remove(String key) {
		attributes.remove(key);
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static void main(String[] args) {
		AttributeMap attributeMap = new AttributeMap();
		attributeMap.set("key", 10);
		attributeMap.set("key2", 1000000000000L);
		attributeMap.set("key3", "abc");
		attributeMap.set("key4", true);
		attributeMap.set("key5", 10.0032D);
		attributeMap.set("key6", 10.0032123F);
		Integer v1 = attributeMap.get("key", AttributeMap.INT);
		Long v2 = attributeMap.get("key2", AttributeMap.LONG);
		String v3 = attributeMap.get("key3", AttributeMap.STRING);
		Boolean v4 = attributeMap.get("key4", AttributeMap.BOOLEAN);
		Double v5 = attributeMap.get("key5", AttributeMap.DOUBLE);
		Float v6 = attributeMap.get("key6", AttributeMap.FLOAT);

		System.out.println(v1);
		System.out.println(v2);
		System.out.println(v3);
		System.out.println(v4);
		System.out.println(v5);
		System.out.println(v6);

//		User user = new User();
//		user.setAge(12);
//		user.setName("狗蛋");
//		attributeMap.set("user", user);
//		User _user = attributeMap.get("user", User.class);
//		System.out.println(_user.toString());

//		Map<Long, Map<Integer, Long>> map = new HashMap<Long, Map<Integer, Long>>();
//		Map<Integer, Long> m = new HashMap<Integer, Long>();
//		m.put(10001, 200000000000000L);
//		map.put(200001L, m);
//		attributeMap.set("aaaa", map);
//
//		Map<Long, Map<Integer, Long>> _map = attributeMap.get("aaaa", AttributeMap.LONG_INT_LONG_MAP);
//		System.out.println(_map);

	}
}
