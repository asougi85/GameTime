package com.shader.gt.api;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TimeQueue {
	private static ExecutorService exe = Executors.newSingleThreadExecutor();
	
	public static void push(Runnable runnable){
		exe.submit(runnable);
	}
	
	public static Future<Runnable> push(Callable<Runnable> callable){
		return exe.submit(callable);
	}
}
