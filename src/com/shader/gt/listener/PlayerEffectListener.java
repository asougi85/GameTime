package com.shader.gt.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.shader.gt.GameTime;
import com.shader.gt.PlayerManager;

public class PlayerEffectListener implements Listener {

	PlayerManager pm = GameTime.getInstance().getPlayers();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent evt) {
		String name = evt.getPlayer().getName();
		if (pm.isBlack(name) || pm.isWaiting(name))
			evt.setCancelled(true);
	}

	@EventHandler
	public void onPlayerBreak(BlockBreakEvent evt) {
		String name = evt.getPlayer().getName();
		if (pm.isBlack(name) || pm.isWaiting(name))
			evt.setCancelled(true);
	}

	@EventHandler
	public void onPlayerPlace(BlockPlaceEvent evt) {
		String name = evt.getPlayer().getName();
		if (pm.isBlack(name) || pm.isWaiting(name))
			evt.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDamage(BlockDamageEvent evt) {
		String name = evt.getPlayer().getName();
		if (pm.isBlack(name) || pm.isWaiting(name))
			evt.setCancelled(true);
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent evt) {
		if (evt.getDamager() instanceof Player) {
			String name = evt.getDamager().getName();
			if (pm.isBlack(name) || pm.isWaiting(name))
				evt.setCancelled(true);
		}
		if (evt.getEntity() instanceof Player) {
			String name = evt.getEntity().getName();
			if (pm.isBlack(name) || pm.isWaiting(name))
				evt.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent evt) {
		String name = evt.getPlayer().getName();
		if (pm.isBlack(name) || pm.isWaiting(name))
			evt.setCancelled(true);
	}

	@EventHandler
	public void onPlayerCMD(PlayerCommandPreprocessEvent evt) {
		String name = evt.getPlayer().getName();
		if (pm.isWaiting(name)) {
			evt.setCancelled(true);
			evt.getPlayer().sendMessage(GameTime.getInstance().getManager().waiting_message);
		} else if (pm.isBlack(name)) {
			if (!evt.getMessage().startsWith("/register") && !evt.getMessage().startsWith("/login")
					&& !evt.getMessage().startsWith("/gt"))
				evt.setCancelled(true);
		}
	}
}
