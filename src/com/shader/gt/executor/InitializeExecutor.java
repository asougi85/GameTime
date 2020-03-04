package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.shader.gt.api.DBExecutor;

public class InitializeExecutor implements DBExecutor {

	private boolean server;

	public InitializeExecutor() {
		server = false;
	}

	public InitializeExecutor(boolean server) {
		this.server = server;
	}

	@Override
	public void run(Connection c) throws SQLException {
		try (Statement state = c.createStatement()) {
			state.setQueryTimeout(30);
			if (server) {
				state.addBatch("CREATE DATABASE IF NOT EXISTS db");
				state.addBatch("USE db");
			}
			state.addBatch(
					"CREATE TABLE IF NOT EXISTS MAP(account tinytext,create_time date,value integer)");
			state.addBatch("CREATE TABLE IF NOT EXISTS LOG(user tinytext,account integer,time date,value integer)");
			state.addBatch("CREATE TABLE IF NOT EXISTS TIME(user tinytext,time integer)");
			state.executeBatch();
			System.out.println("[GameTime]数据库初始化完毕");
		} catch (SQLException e) {
			throw e;
		}
	}
}
