package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.shader.gt.ConfigManager;
import com.shader.gt.GameTime;
import com.shader.gt.Utils;
import com.shader.gt.api.CallableExecutor;

public class UseExecutor implements CallableExecutor {

	ConfigManager config = GameTime.getInstance().getManager();
	String key;
	Player p;
	Object lock = new Object();
	HashMap<String, Long> user = GameTime.getInstance().getPlayers().getPlayers();

	public UseExecutor(Player p, String key) {
		this.key = key;
		this.p = p;
	}

	@Override
	public Runnable run(Connection c) throws SQLException {
		try (Statement state = c.createStatement()) {
			state.execute("SELECT * FROM MAP WHERE account = '" + key + "'");
			ResultSet set = state.getResultSet();
			if (set == null)
				return new Runnable() {
					public void run() {
						p.sendMessage(Utils.toMessage(config.fail_message));
					}
				};
			else {
				long value = set.getLong(3);
				synchronized (lock) {
					long now = user.get(p.getName());
					now += value;
					user.put(p.getName(), now);
				}
				state.execute("DELETE FROM MAP WHERE account = '" + key + "'");
				PreparedStatement ps = c.prepareStatement("INSERT INTO LOG(user,account,time,value) values(?,?,?,?)");
				ps.setString(1, p.getName());
				ps.setString(2, key);
				ps.setDate(3, new Date(System.currentTimeMillis()));
				ps.setLong(4, value);
				ps.execute();
			}
		} catch (SQLException e) {
			throw e;
		}
		return new Runnable() {
			public void run() {
				p.sendMessage(Utils.toMessage(config.success_message));
			}
		};

	}

}
