package com.shader.gt.reader;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.shader.gt.GameTime;
import com.shader.gt.api.LoginReader;

import me.plugins.Login;

public class LoginSystemProReader extends LoginReader {

	ArrayList<String> logged;

	@SuppressWarnings("unchecked")
	public LoginSystemProReader() {
		Login p = (Login) Bukkit.getPluginManager().getPlugin("Login");
		try {
			
			Field f = p.getClass().getDeclaredField("loged");
			f.setAccessible(true);
			logged = (ArrayList<String>) f.get(p);
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasLogined(String name) {
		return logged.contains(name);
	}

	@Override
	public void quit(String name) {
	}

	@Override
	public void execute(GameTime gt) {
		
	}
}
