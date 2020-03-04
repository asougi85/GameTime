package com.shader.gt.api;

import java.sql.Connection;
import java.sql.SQLException;

public interface CallableExecutor {
	public Runnable run(Connection c) throws SQLException;
}
