package com.shader.gt.api;

import org.bukkit.event.Listener;

import com.shader.gt.GameTime;

public abstract class LoginReader implements Listener{
	public abstract boolean hasLogined(String name);
	public abstract void quit(String name);
	public abstract void execute(GameTime gt);
}
