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
//		System.out.println("[GameTime]���ڼ��汾");
//		try {
//			URL u = new URL("http://omwxmouom.bkt.clouddn.com/GameTime.yml");
//			try {
//				InputStream i = u.openConnection().getInputStream();
//				YamlConfiguration f = YamlConfiguration.loadConfiguration(i);
//				String latest = f.getString("version");
//				if (version.equals(latest)) {
//					Bukkit.getConsoleSender().sendMessage(Utils.toMessage("[GameTime]&e�Ѿ������°汾"));
//				} else {
//					if (f.isList("log")) {
//						List<String> list = f.getStringList("log");
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&c&m[GameTime]��⵽���µĸ��£�&a" + latest));
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&e������־��"));
//						for (String s : list) {
//							Bukkit.getConsoleSender().sendMessage(Utils.toMessage(ChatColor.GREEN+"- " + s));
//						}
//					}
//					else{
//						String s = f.getString("log");
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&c&m[GameTime]��⵽���µĸ��£�&a" + latest));
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&e������־��"));
//						Bukkit.getConsoleSender().sendMessage(Utils.toMessage(ChatColor.GREEN+"- " + s));
//					}
//					String address = f.getString("address");
//					Bukkit.getConsoleSender().sendMessage(Utils.toMessage("&e��ǰ��ԭ�����أ�"));
//					Bukkit.getConsoleSender().sendMessage(Utils.toMessage(ChatColor.GREEN+"- "+address));
//				}
//			} catch (IOException e) {
//				System.err.println("�����޷����ӵ���������ȡ���°汾��");
//			}
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
	}
}
