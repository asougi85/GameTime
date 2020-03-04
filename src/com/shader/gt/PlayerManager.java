package com.shader.gt;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.shader.gt.api.TitleSender;
import com.shader.gt.executor.UpdateExecutor;

public class PlayerManager extends BukkitRunnable {

	private static HashMap<String, Long> players = Maps.newHashMap();
	private static HashSet<String> blackPlayers = Sets.newHashSet();
	private static HashSet<String> warningPlayers = Sets.newHashSet();


	public static final int interval = 20 * 5;// refresh the table every 5
												// seconds
	private static final int upload = 20 * 60 * 5;// upload to the database
													// every
													// 5 minutes

	private static final int multiple = upload / interval;

	private int tick = 0;

	private int warning = 0;

	private ConfigManager manager;

	private boolean use_inform;
	private boolean use_title;
	private String inform_message;

	public PlayerManager() {
		manager = GameTime.getInstance().getManager();
		warning = manager.inform_time;
		use_inform = manager.inform;
		use_title = manager.inform_use_title;
		inform_message = manager.inform_message;
	}

	public HashMap<String, Long> getPlayers() {
		return players;
	}

	public boolean isBlack(String name) {
		return blackPlayers.contains(name);
	}

	public boolean isWaiting(String name) {
		return !players.containsKey(name);
	}

	public void quit(String name) {
		players.remove(name);
		blackPlayers.remove(name);
		warningPlayers.remove(name);
	}

	@Override
	public void run() {
		tick++;
		for (String n : players.keySet()) {
			long time = players.get(n);
			if (use_inform) {
				if (time <= warning*60) {
					if (!warningPlayers.contains(n)) {
						if (!use_title) {
							Player p = Bukkit.getPlayer(n);
							p.sendMessage(Utils.toMessage(inform_message, n));
						} else {
							TitleSender.sendTitle(Bukkit.getPlayer(n), 10, 70, 20, n,Utils.toMessage(inform_message, n));
						}
						warningPlayers.add(n);
					}
				} else {
					warningPlayers.remove(n);
				}
			}
			if (time == 0) {
				blackPlayers.add(n);
				continue;
			}
			if (time - 5 <= 0) {
				players.put(n, 0L);
				blackPlayers.add(n);
			} else {
				if (blackPlayers.contains(n))
					blackPlayers.remove(n);
				players.put(n, time - 5);
			}
		}
		if (tick % multiple == 0) {
			GameTime.getInstance().getCon().execute(new UpdateExecutor(players));
		}
	}

}
