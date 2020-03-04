package com.shader.gt.reader;

import java.util.HashSet;

import org.bukkit.event.EventHandler;

import com.google.common.collect.Sets;
import com.shader.gt.GameTime;
import com.shader.gt.api.LoginReader;

import de.st_ddt.crazylogin.events.CrazyLoginLoginEvent;

public class CrazyLoginReader extends LoginReader{

	private static HashSet<String> logged = Sets.newHashSet();
	
	@EventHandler
	public void onCrazyLogin(CrazyLoginLoginEvent evt){
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
