package com.shader.gt.api;

import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Sets;

public class CPURunnable extends BukkitRunnable {

	private static HashSet<Future<Runnable>> set = Sets.newHashSet();

	@Override
	public void run() {
		try {
			for (Future<Runnable> f : set) {
				if (f.isDone()) {
					Runnable r = f.get();
					if (r != null)
						r.run();
					set.remove(f);
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void push(Callable<Runnable> callable) {
		set.add(CallableQueue.push(callable));
	}
}
