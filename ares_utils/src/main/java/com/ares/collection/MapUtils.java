package com.ares.collection;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class MapUtils {

	/**
	 * 创建三个参数的map
	 * 
	 * @param <R>
	 * @param <C>
	 * @param <V>
	 * @return
	 */
	public static <R, C, V> HashBasedTable<R, C, V> createMap() {
		return new HashBasedTable<>(new LinkedHashMap<R, Map<C, V>>());
	}

//    public static <K, V> Map<K, V> newHashMap(K[] keys, V[] values) {
//    public static <K, V> Map<K, V> newHashMap(List<? extends K> keys, List<? extends V> values) {
//        if (keys == null || values == null) {
//            return null;
//        }
//
//        if (keys.size() != values.size()) {
//            throw new IllegalArgumentException("keys和values的的数量不相对");
//        }
//
//        HashMap<K, V> map = new HashMap<>(elements.length);
//
//
//    }

	public static Integer getInt(Map m, String key) {
		try {
			Object o = m.get(key);
			if (o == null)
				return null;
			if (o instanceof Integer)
				return ((Integer) o).intValue();
			else if (o instanceof String) {
				String v = (String) o;
				return Integer.parseInt(v);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static int getInt(Map m, String key, int def) {
		try {
			Object o = m.get(key);
			if (o == null)
				return def;
			if (o instanceof Integer)
				return ((Integer) o).intValue();
			else if (o instanceof String) {
				String v = (String) o;
				return Integer.parseInt(v);
			}
			return def;
		} catch (Exception e) {
			return def;
		}
	}

	public static long getLong(Map m, String key, long def) {
		try {
			Object o = m.get(key);
			if (o == null)
				return def;
			if (o instanceof Long)
				return ((Long) o).longValue();
			else if (o instanceof String) {
				String v = (String) o;
				return Long.parseLong(v);
			}
			return def;
		} catch (Exception e) {
			return def;
		}
	}

	public static double getDouble(Map m, String key, double def) {
		try {
			Object o = m.get(key);
			if (o == null)
				return def;
			if (o instanceof Integer)
				return (Integer) o;
			else if (o instanceof String) {
				String v = (String) o;
				return Double.parseDouble(v);
			} else if (o instanceof Double) {
				Double v = (Double) o;
				return v;
			} else if (o instanceof BigDecimal) {
				BigDecimal v = (BigDecimal) o;
				return v.doubleValue();
			}
			return def;
		} catch (Exception e) {
			return def;
		}
	}

	public static String getString(Map m, String key) {
		try {
			Object o = m.get(key);
			if (o == null)
				return null;
			if (o instanceof String) {
				String v = (String) o;
				if (v.length() <= 0) {
					return null;
				}
				return v;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getString(Map m, String key, String def) {
		try {
			Object o = m.get(key);
			if (o == null)
				return def;
			if (o instanceof String) {
				String v = (String) o;
				if (v.length() <= 0) {
					return def;
				}
				return v;
			}
			return def;
		} catch (Exception e) {
			return def;
		}
	}
}
