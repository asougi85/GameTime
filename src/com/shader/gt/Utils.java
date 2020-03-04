package com.shader.gt;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

public class Utils {

	private static HashMap<String, Long> users = GameTime.getInstance().getPlayers().getPlayers();
	private static ConfigManager config = GameTime.getInstance().getManager();

	public static void reload() {
		users = GameTime.getInstance().getPlayers().getPlayers();
		config = GameTime.getInstance().getManager();
	}

	public static CommandMap getCommandMap() {
		try {
			Method m = Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap", new Class<?>[0]);
			m.setAccessible(true);
			return (CommandMap) m.invoke(Bukkit.getServer(), new Object[] {});
		} catch (Exception e) {
			return null;
		}
	}

	private static String getTime(long time) {
		String base = config.time_format;
		long second = time % 60;
		long minute = ((time - second) / 60) % 60;
		long hour = ((time - second - minute * 60) / 3600) % 24;
		long day = ((time - second - minute * 60 - hour * 24 * 60)/(60*60*24));
		return base.replace("%day%", day + "").replace("%hour%", hour + "").replace("%minute%", minute + "")
				.replace("%second%", second + "");
	}

	public static String toMessage(String arg, String name) {
		String ar = arg.replace("%name%", name);
		if (users.containsKey(name)) {
			long time = users.get(name);
			String tf = getTime(time);
			return toMessage(ar.replace("%time%", tf));
		} else {
			return toMessage(ar);
		}
	}
	
	public static String toMessage(String arg, String name,long value) {
			String tf = getTime(value);
			return toMessage(arg.replace("%time%", tf).replace("%name%", name));
	}
	
	public static String toMessage(String arg) {
		return arg.replace('&', '§');
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	public static String getMinMSG(String name) {
		if (users.containsKey(name)) {
			long time = users.get(name);
			return getMinTime(time);
		} else {
			return "%minute%";
		}
	}
	public static String getMinTime(long time) {
		return time / 60l + "分钟";
	}
	public static String randomKey() {
		return UUID.randomUUID().toString();
	}
}
