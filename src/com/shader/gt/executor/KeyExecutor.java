package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;
import com.shader.gt.Utils;
import com.shader.gt.api.CallableExecutor;

public class KeyExecutor implements CallableExecutor {

	private String key;
	private CommandSender sender;
	ArrayList<String> list = Lists.newArrayList();

	public KeyExecutor(CommandSender sender, String key) {
		this.sender = sender;
		this.key = key;

	}

	@SuppressWarnings("deprecation")
	@Override
	public Runnable run(Connection c) throws SQLException {
		try (Statement state = c.createStatement()) {
			boolean contain = false;
			state.execute("SELECT * FROM MAP WHERE account = '" + key + "'");
			ResultSet map = state.getResultSet();

			if (map.next()) {
				contain = true;
				Date d = map.getDate(2);
				Calendar cc = Calendar.getInstance();
				list.add("Key: " + key);
				list.add("状态: 未使用");
				list.add("价值: " + Utils.getTime(map.getLong(3)));
				DateFormat fmt = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
				list.add("创建时间: " + fmt.format(d));
			}

			if (!contain) {
				return new Runnable() {
					public void run() {
						sender.sendMessage("未找到该Key");
					}
				};
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return new Runnable() {
			public void run() {
				for (String s : list) {
					sender.sendMessage(s);
				}
			}
		};
	}

}
