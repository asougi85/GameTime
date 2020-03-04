package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.shader.gt.GameTime;
import com.shader.gt.api.DBExecutor;

public class ReportExecutor implements DBExecutor {

	private HashMap<String, Long> pm = GameTime.getInstance().getPlayers().getPlayers();
	private final String name;

	public ReportExecutor(String name) {
		this.name = name;
	}

	@Override
	public void run(Connection c) throws SQLException {

		try (Statement state = c.createStatement()) {
			state.execute("SELECT * FROM TIME WHERE user = '" + name + "'");
			ResultSet set = state.getResultSet();
			if (!set.next()) {
				state.executeUpdate("INSERT INTO TIME(user,time) values('" + name + "',0)");
				pm.put(name, 0L);
			} else {
				pm.put(name, set.getLong(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

	}

}
