package com.shader.gt.reader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.shader.gt.GameTime;
import com.shader.gt.api.LoginReader;

import de.mark615.xsignin.XSignIn;
import de.mark615.xsignin.events.EventListener;


public class XsignInReader extends LoginReader{

	Method checkLoggedIn;
	EventListener event;
	
	public XsignInReader() {
		try {
			
			checkLoggedIn = EventListener.class.getDeclaredMethod("checkLoggedIn", new Class<?>[]{Player.class});
			checkLoggedIn.setAccessible(true);
			XSignIn plugin = (XSignIn)Bukkit.getPluginManager().getPlugin("xSignIn");
			Field f = plugin.getClass().getDeclaredField("events");
			f.setAccessible(true);
			event = (EventListener) f.get(plugin);
			
		} catch (SecurityException | IllegalArgumentException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasLogined(String name) {
		try {
			return (boolean)checkLoggedIn.invoke(event, new Object[]{Bukkit.getPlayer(name)});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void quit(String name) {
	}

	@Override
	public void execute(GameTime gt) {
		
	}
}
