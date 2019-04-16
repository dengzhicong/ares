package com.ares.collection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 需实现Serializable，否则就不能指定参数类型
 *
 * @param <R>
 * @param <C>
 * @param <V>
 */
public class HashBasedTable<R, C, V> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final Map<R, Map<C, V>> backingMap;

	public HashBasedTable(Map<R, Map<C, V>> backingMap) {
		this.backingMap = backingMap;
	}

	public V get(Object rowKey, Object columnKey) {
		if ((rowKey == null || columnKey == null)) {
			return null;
		}

		Map<C, V> cvMap = backingMap.get(rowKey);
		if (cvMap == null) {
			return null;
		}

		return cvMap.get(columnKey);
	}

	public V put(R rowKey, C columnKey, V value) {
		checkNotNull(rowKey);
		checkNotNull(columnKey);
		checkNotNull(value);
		return getOrCreate(rowKey).put(columnKey, value);
	}

	public boolean isEmpty() {
		return backingMap.isEmpty();
	}

	public void clear() {
		backingMap.clear();
	}

	/**
	 * 返回被删除的对象
	 *
	 * @param rowKey
	 * @param columnKey
	 * @return
	 */
	public V remove(Object rowKey, Object columnKey) {
		if ((rowKey == null) || (columnKey == null)) {
			return null;
		}
		Map<C, V> cvMap = backingMap.get(rowKey);
		if (cvMap == null) {
			return null;
		}

		V value = cvMap.remove(columnKey);
		if (cvMap.isEmpty()) {
			backingMap.remove(rowKey);
		}
		return value;
	}

	@SuppressWarnings("unused")
	private Map<R, V> removeColumn(Object column) {
		Map<R, V> output = new LinkedHashMap<>();
		Iterator<Map.Entry<R, Map<C, V>>> iterator = backingMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<R, Map<C, V>> entry = iterator.next();
			V value = entry.getValue().remove(column);
			if (value != null) {
				output.put(entry.getKey(), value);
				if (entry.getValue().isEmpty()) {
					iterator.remove();
				}
			}
		}
		return output;
	}

	public boolean containsValue(Object value) {
		if (value == null) {
			return false;
		}
		for (Map<C, V> row : backingMap.values()) {
			if (row.containsValue(value)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsRow(Object rowKey) {
		if (rowKey == null) {
			return false;
		}
		checkNotNull(backingMap);
		try {
			return backingMap.containsKey(rowKey);
		} catch (ClassCastException | NullPointerException e) {
			return false;
		}
	}

	public boolean containsColumn(Object columnKey) {
		if (columnKey == null) {
			return false;
		}
		for (Map<C, V> map : backingMap.values()) {
			if (safeContainsKey(map, columnKey)) {
				return true;
			}
		}
		return false;
	}

	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}

	private Map<C, V> getOrCreate(R rowKey) {
		Map<C, V> map = backingMap.get(rowKey);
		if (map == null) {
			map = new HashMap<>();
			backingMap.put(rowKey, map);
		}
		return map;
	}

	private boolean safeContainsKey(Map<?, ?> map, Object key) {
		checkNotNull(map);
		try {
			return map.containsKey(key);
		} catch (ClassCastException | NullPointerException e) {
			return false;
		}
	}
}
