package com.shader.gt.api;

public interface Connector {
	public void execute(DBExecutor exe);
	public void execute(CallableExecutor ce);
}
