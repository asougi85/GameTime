package com.shader.gt.update;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import com.shader.gt.Utils;

import net.md_5.bungee.api.ChatColor;

public class UpdateHelper implements Runnable {

	private String version;

	public UpdateHelper(String version) {
		this.version = version;
	}
	
	
	public long Random() {
		long l = (long)(Math.random()*100000000L);
		return l;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
//		System.out.println("[GameTime]正在检查版本");
//		try {
//			URL u = new URL("http://omwxmouom.bkt.clouddn.com/GameTime.yml");
//			try {
//				InputStream i = u.openConnection().getInputStream();
//				YamlConfiguration f = YamlConfiguration.loadConfiguration(i);
//				String latest = f.getString("version");
//				if (version.equals(latest)) {
//					Bukkit.getConsoleSender().sendMessage(Utils.toMessage("[GameTime]&e已经是最新版本"));
//				} else {
//					if (f.isList("log")) {
//						List<String> list = f.getStringList("log");
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&c&m[GameTime]检测到有新的更新：&a" + latest));
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&e更新日志："));
//						for (String s : list) {
//							Bukkit.getConsoleSender().sendMessage(Utils.toMessage(ChatColor.GREEN+"- " + s));
//						}
//					}
//					else{
//						String s = f.getString("log");
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&c&m[GameTime]检测到有新的更新：&a" + latest));
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&e更新日志："));
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage(ChatColor.GREEN+"- " + s));
//					}
//					String address = f.getString("address");
//					Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&e请前往原帖下载："));
//					Bukkit.getConsoleSender().sendMessage(Utils.toMessage(ChatColor.GREEN+"- "+address));
//				}
//			} catch (IOException e) {
//				System.err.println("错误，无法连接到服务器获取最新版本号");
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
	}
}
