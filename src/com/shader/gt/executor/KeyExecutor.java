package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.shader.gt.api.CallableExecutor;

public class KeyExecutor implements CallableExecutor {

	private String key;

	public KeyExecutor(String key) {
		this.key = key;

	}

	@SuppressWarnings("deprecation")
	@Override
	public Runnable run(Connection c) throws SQLException {
		try (Statement state = c.createStatement()) {
			boolean contain = false;
			state.execute("SELECT * FROM MAP WHERE account = '" + key + "'");
			ResultSet map = state.getResultSet();
			if (map != null) {
				if (map.next()) {
					contain = true;
					Date d = map.getDate(2);
					System.out.println("Key: " + key);
					System.out.println("状态: 未使用");
					System.out.println("价值: " + map.getLong(3) + "秒");
					System.out.println("创建时间: " + d.toLocaleString());
				}
			}

			state.execute("SELECT * FROM LOG WHERE account = '" + key + "'");
			map = state.getResultSet();
			if (map != null) {
				if (map.next()) {
					contain = true;
					Date d = map.getDate(3);
					System.out.println("Key: " + key);
					System.out.println("状态: 被" + map.getString(1) + "使用");
					System.out.println("价值: " + map.getLong(4) + "秒");
					System.out.println("使用时间: " +d.toLocaleString());
				}
			}
			if (!contain) {
				System.out.println("未找到该Key");
				return new Runnable() {
					public void run() {
					}
				};
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}

}
