package com.shader.gt;

import java.io.File;

import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.shader.gt.api.CPURunnable;
import com.shader.gt.api.Connector;
import com.shader.gt.api.DBConnector;
import com.shader.gt.api.SQLConnector;
import com.shader.gt.command.GTCommand;
import com.shader.gt.executor.InitializeExecutor;
import com.shader.gt.executor.UpdateExecutor;
import com.shader.gt.listener.PlayerChangeListener;
import com.shader.gt.listener.PlayerEffectListener;

public class GameTime extends JavaPlugin {

	private CommandMap commandMap;
	private ConfigManager manager;
	private CPURunnable cpu;
	private static GameTime instance;
	private boolean first = true;
	
	public ConfigManager getManager() {
		return manager;
	}

	private PlayerManager players;

	public PlayerManager getPlayers() {
		return players;
	}

	private Connector con;

	public Connector getCon() {
		return con;
	}

	@Override
	public void onEnable() {
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);
		if(players != null)
			players.cancel();
		if(cpu != null)
			cpu.cancel();
		instance = this;
		loadConfig();
		players = new PlayerManager();
		cpu = new CPURunnable();
		loadConnector();
		players.runTaskTimer(this, 0, PlayerManager.interval);
		cpu.runTaskTimer(this,0, 10);
		if(first){
		getServer().getPluginManager().registerEvents(new PlayerChangeListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerEffectListener(), this);
		loadClass();
		commandMap = Utils.getCommandMap();
		commandMap.register(this.getDescription().getName(), new GTCommand(this));
		}
		Utils.reload();
		first = false;
		System.out.println("[GameTime]插件加载成功");
	}
	
	public void onDisable(){
		super.onDisable();
		con.execute(new UpdateExecutor(players.getPlayers()));
	}
	
	private void loadClass(){
		try {
			Class.forName("com.shader.gt.executor.CreateExecutor");
			Class.forName("com.shader.gt.executor.ExitExecutor");
			Class.forName("com.shader.gt.executor.ExportExecutor");
			Class.forName("com.shader.gt.executor.IncreaseExecutor");
			Class.forName("com.shader.gt.executor.InitializeExecutor");
			Class.forName("com.shader.gt.executor.KeyExecutor");
			Class.forName("com.shader.gt.executor.ReportExecutor");
			Class.forName("com.shader.gt.executor.UpdateExecutor");
			Class.forName("com.shader.gt.executor.UseExecutor");
			Class.forName("org.bstats.bukkit.Metrics");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void loadConnector() {
		if (!manager.use_mysql)
			con = new DBConnector("jdbc:sqlite:database.db");
		else {
			String url = "jdbc:mysql://" + manager.address + ":" + manager.port + "/mysql?&useSSL=true";
			con = new SQLConnector(url, manager.user, manager.password);
		}
		InitializeExecutor exe = new InitializeExecutor();
		con.execute(exe);
	}

	private void loadConfig() {
		File f = new File(getDataFolder(), "config.yml");
		if (!f.exists())
			this.saveDefaultConfig();
		manager = new ConfigManager(YamlConfiguration.loadConfiguration(f));
	}

	public static GameTime getInstance() {
		return instance;
	}

	public boolean isServer() {
		return manager.use_mysql;
	}
	

	
}
