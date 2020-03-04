package com.shader.gt.reader;

import java.util.HashSet;

import org.bukkit.event.EventHandler;

import com.google.common.collect.Sets;
import com.shader.gt.GameTime;
import com.shader.gt.api.LoginReader;

import fr.xephi.authme.events.LoginEvent;

public class AuthMeReader extends LoginReader{

	private static HashSet<String> logged = Sets.newHashSet();

	@EventHandler
	public void onAuthMeLogin(LoginEvent evt) {
		logged.add(evt.getPlayer().getName());
	}

	@Override
	public boolean hasLogined(String name) {
		return logged.contains(name);
	}

	@Override
	public void quit(String name) {
		logged.remove(name);
	}

	@Override
	public void execute(GameTime gt) {
		gt.getServer().getPluginManager().registerEvents(this, gt);
	}
}
