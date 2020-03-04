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
				System.out.println("[GameTime]��⵽��ʹ����1.8���µ���Ϸ�汾:"+ m.group(0) +"�����Զ��ر�Title����");
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
			System.out.println("[GameTime]��⵽��ʹ����PAPI�������������ɱ���ע��");
		}
		System.out.println("[GameTime]������سɹ�");
		
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
				System.out.println("[GameTime]��½�����ѿ���,���Ĭ���ڵ�½�Ժ�ſ�ʼ�۷�,��Ҫ�ر����������ļ����޸�");
				loginType = LoginType.valueOf(n);
				if (loginType.equals(LoginType.AuthMe)) {
					System.out.println("[GameTime]��⵽��ʹ����AuthMe��½�������ʼ��������");
					loginReader = new AuthMeReader();
					loginReader.execute(this);
					System.out.println("[GameTime]�����������");
				}

				else if (loginType.equals(LoginType.CrazyLogin)) {
					System.out.println("[GameTime]��⵽��ʹ����CrazyLogin��½�������ʼ��������");
					loginReader = new CrazyLoginReader();
					loginReader.execute(this);
					System.out.println("[GameTime]�����������");
				}

				else if (loginType.equals(LoginType.Login)) {
					System.out.println("[GameTime]��⵽��ʹ����LoginSystemPro��½�������ʼ��������");
					loginReader = new LoginSystemProReader();
					loginReader.execute(this);
					System.out.println("[GameTime]�����������");
				}

				else if (loginType.equals(LoginType.xSignIn)) {
					System.out.println("[GameTime]��⵽��ʹ����xSignIn��½�������ʼ��������");
					loginReader = new XsignInReader();
					loginReader.execute(this);
					System.out.println("[GameTime]�����������");
				}
				return true;
			}
		}
		System.out.println("[GameTime]δ��⵽���õ�½���,����ȡ��");
		return false;
	}

	private boolean isLoad(String name) {
		return getServer().getPluginManager().getPlugin(name) != null;
	}

	enum LoginType {
		AuthMe, CrazyLogin, xSignIn, Login
	}
}
