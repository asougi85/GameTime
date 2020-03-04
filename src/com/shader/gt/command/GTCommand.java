package com.shader.gt.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.shader.gt.GameTime;
import com.shader.gt.Utils;
import com.shader.gt.executor.CheckExecutor;
import com.shader.gt.executor.CreateExecutor;
import com.shader.gt.executor.ExportExecutor;
import com.shader.gt.executor.KeyExecutor;
import com.shader.gt.executor.UseExecutor;

public class GTCommand extends Command {

	public static final String name = "gt";
	public static final String usage = "/gt help";
	public static final String description = "帮助";
	public static final List<String> aliases = Arrays.asList(new String[0]);
	public static final String[] helpMessage = { "/gt help #帮助内容", "/gt use [key] #使用Key", "/gt check #查询剩余时间",
			"/gt [key] #检查[Key]的状态", "/gt check [name] #查询指定玩家剩余时间",
			"/gt create [value] [amount] #新建指定数量、指定价值的key,限后台执行",
			"/gt export <value> #导出所有指定价值的key,限后台执行(不填写参数默认导出所有key)" };

	public GameTime gt;
	public HashMap<String, Long> map;

	public GTCommand(GameTime gt) {
		super(name, description, usage, aliases);
		this.gt = gt;
		map = gt.getPlayers().getPlayers();
	}

	public void help(CommandSender p) {
		p.sendMessage(helpMessage);
	}

	public void refuse(CommandSender p) {
		p.sendMessage("你没有权限做这件事");
	}

	public void tell(CommandSender p) {
		p.sendMessage("====GameTime插件====");
		p.sendMessage("作者: UnkownDomain");
		p.sendMessage("版本: " + gt.getDescription().getVersion());
	}
	
	public void wrong(CommandSender p){
		p.sendMessage("参数错误！");
	}
	public void check(CommandSender p, String name) {
		if(map.containsKey(name))
			p.sendMessage(Utils.toMessage(gt.getManager().check_message, name));
		else
			GameTime.getInstance().getCon().execute(new CheckExecutor(p,name));
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args == null) {
			sender.sendMessage(helpMessage);
			return true;
		}

		final int length = args.length;
		if (length != 0 && length != 1 && length != 2 && length != 3) {
			help(sender);
			return true;
		}

		if (length == 0) {
			if (sender.hasPermission("gametime.gt")) {
				tell(sender);
			} else {
				help(sender);
			}
		}

		if (length == 1)
			switch (args[0]) {
			case "help":
				if (sender.hasPermission("gametime.help"))
					help(sender);
				else
					refuse(sender);
				return true;

			case "check":
				if (sender instanceof Player) {
					if (sender.hasPermission("gametime.check")) {
						check(sender, sender.getName());
					} else
						refuse(sender);
				} else
					sender.sendMessage("后台请使用/gt check [玩家]");
				return true;
			case "export":
				if(sender instanceof Player){
					refuse(sender);
				}
				else{
					GameTime.getInstance().getCon().execute(new ExportExecutor(sender));
				}
				return true;

			case "reload":
				if (sender instanceof Player)
					refuse(sender);
				else
					gt.onEnable();
				return true;
			default:
				help(sender);
			}

		if (length == 2) {
			switch (args[0]) {
			case "use":
				if (sender instanceof Player) {
					if(sender.hasPermission("gametime.use")){
						GameTime.getInstance().getCon().execute(new UseExecutor((Player)sender,args[1]));
					}else
						refuse(sender);
				} else {
					refuse(sender);
				}

				return true;
			case "check":
				if (sender.hasPermission("gametime.checkother"))
					check(sender, args[1]);
				else
					refuse(sender);
				return true;
			case "key":
				if(sender instanceof Player)
					refuse(sender);
				else
					GameTime.getInstance().getCon().execute(new KeyExecutor(args[1]));
				return true;
			case "export":
				if(sender instanceof Player){
					refuse(sender);
				}
				else{
					if(Utils.isNumeric(args[1]))
						GameTime.getInstance().getCon().execute(new ExportExecutor(sender,Long.parseLong(args[1])));
					else
						wrong(sender);
				}
				return true;
			default:
				help(sender);
				return true;
			}
		}

		if (length == 3) {
			switch (args[0]) {
			case "create":
				if(sender instanceof Player)
					refuse(sender);
					else{
						if(Utils.isNumeric(args[1])&&Utils.isNumeric(args[2]))
							GameTime.getInstance().getCon().execute(new CreateExecutor(Long.parseLong(args[1]),Integer.parseInt(args[2])));
						else
							wrong(sender);
					}
				return true;
			default:
				return true;
			}
		}
		return true;
	}
}
