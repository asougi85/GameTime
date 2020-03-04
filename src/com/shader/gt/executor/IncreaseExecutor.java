package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.shader.gt.GameTime;
import com.shader.gt.api.DBExecutor;

public class IncreaseExecutor implements DBExecutor {

	private static HashMap<String, Long> pm = GameTime.getInstance().getPlayers().getPlayers();
	private final String name;
	private final int value;
	
	public IncreaseExecutor(String name,int value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void run(Connection c) throws SQLException{
		try (Statement state = c.createStatement()) {
			long nv = pm.get(name).longValue()+value;
			state.executeUpdate("UPDATE TIME SET time = "+nv+" WHERE user = '" + name + "'");
			pm.put(name, nv);
		} catch (SQLException e) {
			 throw e;
		}
	}

}
