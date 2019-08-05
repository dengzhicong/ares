package com.ares.thread;

public class HandlerManagerTest {
	public static void handle(Object packet) {
		System.out.println("HandlerManagerTest.handle，线程：" + Thread.currentThread().getName());
	}
}
