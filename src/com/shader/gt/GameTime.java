package com.shader.gt;

import java.io.File;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Sets;
import com.shader.gt.api.CPURunnable;
import com.shader.gt.api.Connector;
import com.shader.gt.api.DBConnector;
import com.shader.gt.api.LoginReader;
import com.shader.gt.api.SQLConnector;
import com.shader.gt.api.TimeQueue;
import com.shader.gt.bState.Metrics;
import com.shader.gt.command.GTCommand;
import com.shader.gt.executor.InitializeExecutor;
import com.shader.gt.executor.UpdateExecutor;
import com.shader.gt.listener.PlayerChangeListener;
import com.shader.gt.listener.PlayerEffectListener;
import com.shader.gt.reader.AuthMeReader;
import com.shader.gt.reader.CrazyLoginReader;
import com.shader.gt.reader.LoginSystemProReader;
import com.shader.gt.reader.XsignInReader;
import com.shader.gt.update.UpdateHelper;

public class GameTime extends JavaPlugin {

	private CommandMap commandMap;
	private ConfigManager manager;
	private CPURunnable cpu;
	private static GameTime instance;
	private HashSet<Class<?>> classes = Sets.newHashSet();
	public boolean isFitForTitle = true;
	private final Pattern verPat = Pattern.compile("[(]MC: [0-9]+\\.([0-9]+).*[)]");
	
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
	
	public static void main(String[] args) {

	}

	public void setFitForTitle(){
		String version = Bukkit.getVersion();
		Matcher m = verPat.matcher(version);
		if(m.find()){
			String vex = m.group(1);
			int i = Integer.parseInt(vex);
			if(i<=7){
				isFitForTitle = false;
				System.out.println("[GameTime]检测到您使用了1.8以下的游戏版本:"+ m.group(0) +"，将自动关闭Title功能");
			}
		}
	}
	
	public void reload() {
		loadConfig();
		players = new PlayerManager();
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		setFitForTitle();
		TimeQueue.push(new UpdateHelper(this.getDescription().getVersion()));
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this);
		instance = this;
		loadConfig();
		players = new PlayerManager();
		cpu = new CPURunnable();
		loadConnector();
		players.runTaskTimer(this, 0, PlayerManager.interval);
		cpu.runTaskTimer(this, 0, 10);
		if (manager.use_login_hook)
			listen_login = getListenLogin();
		getServer().getPluginManager().registerEvents(new PlayerChangeListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerEffectListener(), this);
		loadClass();
		commandMap = Utils.getCommandMap();
		commandMap.register(this.getDescription().getName(), new GTCommand(this));
		Utils.reload();
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
			new PapiVarities(this).register();
			System.out.println("[GameTime]检测到您使用了PAPI变量插件，已完成变量注册");
		}
		System.out.println("[GameTime]插件加载成功");
		
	}

	public void onDisable() {
		super.onDisable();
		con.execute(new UpdateExecutor(players.getPlayers()));
	}

	private void loadClass() {
		classes.add(com.shader.gt.executor.UpdateExecutor.class);
		classes.add(com.shader.gt.executor.KeyExecutor.class);
	}

	private void loadConnector() {
		boolean server = manager.use_mysql;
		if (!server)
			con = new DBConnector("jdbc:sqlite:GameTime.db");
		else {
			String url = "jdbc:mysql://" + manager.address + ":" + manager.port + "/mysql?&useSSL=true";
			con = new SQLConnector(url, manager.user, manager.password);
		}
		InitializeExecutor exe = new InitializeExecutor(server);
		con.execute(exe);
		if(con instanceof SQLConnector){
			((SQLConnector) con).setUrl("jdbc:mysql://" + manager.address + ":" + manager.port + "/"+manager.database+"?&useSSL=true");
		}
	}

	private void loadConfig() {
		File f = new File(getDataFolder(), "config.yml");
		this.saveDefaultConfig();
		manager = new ConfigManager(YamlConfiguration.loadConfiguration(f));
	}

	public static GameTime getInstance() {
		return instance;
	}

	public boolean isServer() {
		return manager.use_mysql;
	}

	private LoginType loginType = null;
	private LoginReader loginReader = null;

	public LoginReader getLoginReader() {
		return loginReader;
	}

	private boolean listen_login = false;

	public boolean isListenLogin() {
		return listen_login;
	}

	private boolean getListenLogin() {
		String[] names = { "AuthMe", "CrazyLogin", "xSignIn", "Login" };
		for (String n : names) {
			if (isLoad(n)) {
				System.out.println("[GameTime]登陆互联已开启,玩家默认在登陆以后才开始扣费,需要关闭请在配置文件中修改");
				loginType = LoginType.valueOf(n);
				if (loginType.equals(LoginType.AuthMe)) {
					System.out.println("[GameTime]检测到您使用了AuthMe登陆插件，开始构建互联");
					loginReader = new AuthMeReader();
					loginReader.execute(this);
					System.out.println("[GameTime]构建互联完毕");
				}

				else if (loginType.equals(LoginType.CrazyLogin)) {
					System.out.println("[GameTime]检测到您使用了CrazyLogin登陆插件，开始构建互联");
					loginReader = new CrazyLoginReader();
					loginReader.execute(this);
					System.out.println("[GameTime]构建互联完毕");
				}

				else if (loginType.equals(LoginType.Login)) {
					System.out.println("[GameTime]检测到您使用了LoginSystemPro登陆插件，开始构建互联");
					loginReader = new LoginSystemProReader();
					loginReader.execute(this);
					System.out.println("[GameTime]构建互联完毕");
				}

				else if (loginType.equals(LoginType.xSignIn)) {
					System.out.println("[GameTime]检测到您使用了xSignIn登陆插件，开始构建互联");
					loginReader = new XsignInReader();
					loginReader.execute(this);
					System.out.println("[GameTime]构建互联完毕");
				}
				return true;
			}
		}
		System.out.println("[GameTime]未检测到可用登陆插件,互联取消");
		return false;
	}

	private boolean isLoad(String name) {
		return getServer().getPluginManager().getPlugin(name) != null;
	}

	enum LoginType {
		AuthMe, CrazyLogin, xSignIn, Login
	}
}
