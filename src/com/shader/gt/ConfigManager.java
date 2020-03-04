package com.shader.gt;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	public final boolean inform;
	public final boolean inform_use_title;
	public final boolean use_mysql;

	public final int inform_time;
	public final int port;

	public final String time_format;
	public final String inform_message;

	public final String address;
	public final String user;
	public final String password;

	public final String waiting_message;
	public final String check_message;
	public final String check_missing_message;
	public final String success_message;
	public final String fail_message;

	public final String database;

	public final long original_time;

	public final boolean cost_time;
	public final boolean op_can_use_add;
	public final boolean use_login_hook;

	public ConfigManager(YamlConfiguration config) {
		inform = config.getBoolean("inform");
		inform_use_title = config.getBoolean("inform_use_title");
		use_mysql = config.getBoolean("use_mysql");

		inform_time = config.getInt("inform_time");
		port = config.getInt("port");

		time_format = config.getString("time_format");
		inform_message = config.getString("inform_message");
		
		address = config.getString("address");
		user = config.getString("user");
		password = config.getString("password");
		
		waiting_message = config.getString("waiting_message");
		check_message = config.getString("check_message");
		check_missing_message = config.getString("check_missing_message");
		success_message = config.getString("success_message");
		fail_message = config.getString("fail_message");
		
		
		database = config.getString("database");
		
		if(!config.contains("original_time")){
			config.set("original_time", 30);
			try {
				config.save(new File(GameTime.getInstance().getDataFolder(),"config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(!config.contains("cost_time")){
			config.set("cost_time", true);
			try {
				config.save(new File(GameTime.getInstance().getDataFolder(),"config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(!config.contains("op_can_use_add")){
			config.set("op_can_use_add", false);
			try {
				config.save(new File(GameTime.getInstance().getDataFolder(),"config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(!config.contains("use_login_hook")){
			config.set("use_login_hook", true);
			try {
				config.save(new File(GameTime.getInstance().getDataFolder(),"config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		op_can_use_add = config.getBoolean("op_can_use_add");
		
		original_time = config.getLong("original_time");

		cost_time = config.getBoolean("cost_time");
		
		use_login_hook = config.getBoolean("use_login_hook");
	}
}
