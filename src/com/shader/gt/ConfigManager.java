package com.shader.gt;

import org.bukkit.configuration.Configuration;

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
	
	public ConfigManager(Configuration config) {
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
	}
}
