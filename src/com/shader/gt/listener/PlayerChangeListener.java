package com.shader.gt.listener;

import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.shader.gt.GameTime;
import com.shader.gt.api.Connector;
import com.shader.gt.executor.ExitExecutor;
import com.shader.gt.executor.ReportExecutor;

public class PlayerChangeListener implements Listener{

	GameTime gt = GameTime.getInstance();
	HashMap<String,Long> pm = gt.getPlayers().getPlayers();
	Connector con = gt.getCon();
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt){
		con.execute(new ReportExecutor(evt.getPlayer().getName()));		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent evt){
		con.execute(new ExitExecutor(evt.getPlayer().getName()));
	}
}
