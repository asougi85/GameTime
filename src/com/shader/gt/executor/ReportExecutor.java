package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.shader.gt.GameTime;
import com.shader.gt.Utils;
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
			if(GameTime.getInstance().getManager().use_mysql){
				state.execute("USE "+GameTime.getInstance().getManager().database);
			}
			state.execute("SELECT * FROM TIME WHERE user = '" + name + "'");
			ResultSet set = state.getResultSet();
			if (!set.next()) {
				long time = GameTime.getInstance().getManager().original_time*60;
				state.executeUpdate("INSERT INTO TIME(user,time) values('" + name + "',"+time+")");
				pm.put(name, time);
			} else {
				pm.put(name, set.getLong(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

	}

}
