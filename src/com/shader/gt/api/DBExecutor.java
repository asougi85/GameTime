package com.shader.gt.api;

import java.sql.Connection;
import java.sql.SQLException;

public interface DBExecutor {
	public void run(Connection c) throws SQLException;
}
