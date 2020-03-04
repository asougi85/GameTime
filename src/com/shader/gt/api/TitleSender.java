package com.shader.gt.api;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import static com.comphenix.protocol.utility.MinecraftReflection.*;
import java.lang.reflect.Constructor;

public class TitleSender {
	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getPacketClass()).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {
		try {
			Object e;
			Object chatTitle;
			Object chatSubtitle;
			Constructor<?> subtitleConstructor;
			Object titlePacket;
			Object subtitlePacket;

			if (title != null) {
				title = ChatColor.translateAlternateColorCodes('&', title);
				title = title.replaceAll("%player%", player.getDisplayName());
				// Times packets
				e = getMinecraftClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
				chatTitle = getMinecraftClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class }).invoke(null, "{\"text\":\"" + title + "\",\"color\":\"yellow\"}");
				subtitleConstructor = getMinecraftClass("PacketPlayOutTitle").getConstructor(
						getMinecraftClass("PacketPlayOutTitle").getDeclaredClasses()[0],
						getMinecraftClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
				titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut);
				sendPacket(player, titlePacket);

				e = getMinecraftClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
				chatTitle = getMinecraftClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class }).invoke(null, "{\"text\":\"" + title + "\",\"color\":\"yellow\"}");
				subtitleConstructor = getMinecraftClass("PacketPlayOutTitle").getConstructor(
						getMinecraftClass("PacketPlayOutTitle").getDeclaredClasses()[0],
						getMinecraftClass("IChatBaseComponent"));
				titlePacket = subtitleConstructor.newInstance(e, chatTitle);
				sendPacket(player, titlePacket);
			}

			if (subtitle != null) {
				subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
				subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
				// Times packets
				e = getMinecraftClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
				chatSubtitle = getMinecraftClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class }).invoke(null, "{\"text\":\"" + title + "\",\"color\":\"red\"}");
				subtitleConstructor = getMinecraftClass("PacketPlayOutTitle").getConstructor(
						getMinecraftClass("PacketPlayOutTitle").getDeclaredClasses()[0],
						getMinecraftClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
				subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
				sendPacket(player, subtitlePacket);

				e = getMinecraftClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
				chatSubtitle = getMinecraftClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class }).invoke(null, "{\"text\":\"" + subtitle + "\",\"color\":\"red\"}");
				subtitleConstructor = getMinecraftClass("PacketPlayOutTitle").getConstructor(
						getMinecraftClass("PacketPlayOutTitle").getDeclaredClasses()[0],
						getMinecraftClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
				subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
				sendPacket(player, subtitlePacket);
			}
		} catch (Exception var11) {
			var11.printStackTrace();
		}
	}

	public static void clearTitle(Player player) {
		sendTitle(player, 0, 0, 0, "", "");
	}
}