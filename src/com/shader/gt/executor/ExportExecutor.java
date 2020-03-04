package com.shader.gt.executor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Sets;
import com.shader.gt.GameTime;
import com.shader.gt.Utils;
import com.shader.gt.api.CallableExecutor;
import com.shader.gt.api.ExportQueue;

public class ExportExecutor implements CallableExecutor {

	long value;
	CommandSender sender;

	public ExportExecutor(CommandSender sender) {
		this(sender, -1L);
	}

	public ExportExecutor(CommandSender sender, long value) {
		this.sender = sender;
		this.value = value;
	}

	@Override
	public Runnable run(Connection c) throws SQLException {
		try (Statement state = c.createStatement()) {
			if (value == -1L)
				state.execute("SELECT * FROM MAP");
			else {
				state.execute("SELECT * FROM MAP WHERE value=" + value);
			}
			ResultSet set = state.getResultSet();
			if (!set.next())
				return new Runnable() {
					public void run() {
						sender.sendMessage("未找到指定的Key");
					}
				};
				
			final HashSet<String> texts = Sets.newHashSet();
			do {
				String account = set.getString(1);
				texts.add(account);
			} while (set.next());
			
			if (texts.isEmpty())
				return new Runnable() {
					public void run() {
						sender.sendMessage("未找到指定的Key");
					}
				};
			ExportQueue.push(new Runnable() {
				public void run() {
					File gt = new File(GameTime.getInstance().getDataFolder(), "key.txt");
					if (gt.exists())
						gt.delete();
					try {
						gt.createNewFile();
						FileWriter fileWritter = new FileWriter(gt, true);
						BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
						for (String s : texts) {
							bufferWritter.write(s + "\n");
						}
						bufferWritter.flush();
						bufferWritter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (SQLException e) {
			throw e;
		}
		return new Runnable() {
			@Override
			public void run() {
				sender.sendMessage("Key表已经导出完成！");
			}
		};
	}

}
