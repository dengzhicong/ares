package com.ares.thread;

import java.lang.reflect.Method;

import com.ares.thread.thread_grain.AsyncThreadManager;
import com.ares.thread.thread_grain.ThreadHandle;

public class ThreadTest {
	public static void main(String[] args) throws Exception {
		AsyncThreadManager.init(100, 10, 3, 0);
		AsyncThreadManager.start();
		
		PacketTest packetTest = new PacketTest();
		Method method = HandlerManagerTest.class.getMethod("handle", new Class[] { Object.class });
		ThreadHandle threadHandle = new ThreadHandle(packetTest, method, null);
		boolean result1 = AsyncThreadManager.addHandle(threadHandle, 1, 1);
		System.err.println("加入threadHandle是否成功："+result1);
		
		CycleTest cycleTest = new CycleTest();
		cycleTest.name = "testAddCycle";
		boolean result2 = AsyncThreadManager.addCycle(cycleTest, 1, 1);
		
		System.err.println("加入cycleTest是否成功："+result2);
	}
}
