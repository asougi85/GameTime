package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.shader.gt.Utils;
import com.shader.gt.api.CallableExecutor;

public class CreateExecutor implements CallableExecutor {

	private long time;
	private int amount;

	public CreateExecutor(long time, int amount) {
		this.time = time;
		this.amount = amount;
	}

	@Override
	public Runnable run(Connection c) throws SQLException {
		Date d = new Date(System.currentTimeMillis());
		
		try (PreparedStatement state = c.prepareStatement("INSERT INTO MAP(account,create_time,value) values(?,?,?)")) {
			c.setAutoCommit(false);
			for (int i = 0; i < amount; i++) {
				state.setString(1, Utils.randomKey());
				state.setDate(2,d);
				state.setLong(3, time);
				state.addBatch();
			}
			state.executeBatch();
			c.commit();
			c.setAutoCommit(true);
			System.out.println("Ìí¼Ó³É¹¦");
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}

}
