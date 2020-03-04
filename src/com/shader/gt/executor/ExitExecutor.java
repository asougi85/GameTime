package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.shader.gt.GameTime;
import com.shader.gt.api.DBExecutor;

public class ExitExecutor implements DBExecutor {

	private HashMap<String, Long> pm = GameTime.getInstance().getPlayers().getPlayers();
	private final String name;

	public ExitExecutor(String name) {
		this.name = name;
	}

	@Override
	public void run(Connection c) throws SQLException{
		try (Statement state = c.createStatement()) {
			state.executeUpdate("UPDATE TIME SET time = "+pm.get(name).longValue()+" WHERE user = '" + name + "'");
			GameTime.getInstance().getPlayers().quit(name);
		} catch (SQLException e) {
			 throw e;
		}

	}

}
