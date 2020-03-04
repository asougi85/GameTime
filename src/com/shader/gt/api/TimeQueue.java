package com.shader.gt.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimeQueue {
	private static ExecutorService exe = Executors.newSingleThreadExecutor();
	
	public static void push(Runnable runnable){
		exe.submit(runnable);
	}
	
}
