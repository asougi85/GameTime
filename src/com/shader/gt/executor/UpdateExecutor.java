package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;

import com.shader.gt.Utils;
import com.shader.gt.api.DBExecutor;

public class UpdateExecutor implements DBExecutor {

	private HashMap<String, Long> pm;

	public UpdateExecutor(HashMap<String,Long> players){
		pm = players;
	}
	
	@Override
	public void run(Connection c) throws SQLException {
		try (PreparedStatement state = c.prepareStatement("UPDATE TIME SET time = ? WHERE user = ?")) {
			c.setAutoCommit(false);
			update: {
				Set<String> set = pm.keySet();
				if (set.isEmpty())
					break update;
				for (String name : set) {
					state.setLong(1, pm.get(name));
					state.setString(2, name);
					state.addBatch();
					;
					state.executeBatch();
				}
				c.commit();
			}
			c.setAutoCommit(true);
		} catch (SQLException e) {
			throw e;
		}
	}

}
