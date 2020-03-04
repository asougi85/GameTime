package com.shader.gt;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PapiVarities extends PlaceholderExpansion {

	private GameTime plugin;

	public PapiVarities(GameTime plugin) {
		
		this.plugin = plugin;
	}

	@Override
	public String onPlaceholderRequest(Player p, String varity) {
		if (varity.equals("GameTimeRemain")) {
			return Utils.toMessage("%time%", p.getName());
		}
		if (varity.equals("GameTimeRemainMin")) {
			return Utils.getMinMSG(p.getName());
		}
		return null;
	}

	@Override
	public String getAuthor() {
		return "asougi85";
	}

	@Override
	public String getIdentifier() {
		return "gametimeVarities";
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}
}
