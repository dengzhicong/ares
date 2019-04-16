package com.ares.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ListUtils {

	public static <E> ArrayList<E> newArrayList(E... elements) {
		if (elements == null) {
			return null;
		}
		if (elements.length < 0) {
			return null;
		}
		ArrayList<E> list = new ArrayList<>(elements.length);
		Collections.addAll(list, elements);
		return list;
	}

	public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
		if (elements == null) {
			return null;
		}
		LinkedList<E> list = new LinkedList();
		elements.forEach(e -> list.add(e));

		return list;
	}
}
