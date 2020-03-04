package com.shader.gt.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.shader.gt.GameTime;
import com.shader.gt.Utils;
import com.shader.gt.executor.AddExecutor;
import com.shader.gt.executor.CheckExecutor;
import com.shader.gt.executor.CreateExecutor;
import com.shader.gt.executor.ExportExecutor;
import com.shader.gt.executor.KeyExecutor;
import com.shader.gt.executor.UseExecutor;

import net.md_5.bungee.api.ChatColor;

public class GTCommand extends Command {

	public static final String name = "gt";
	public static final String usage = "/gt help";
	public static final String description = "����";
	public static final List<String> aliases = Arrays.asList(new String[0]);
	public static final String[] playerHelpMessage = { "/gt help #��������", "/gt use [key] #ʹ��Key", "/gt check #��ѯʣ��ʱ��" };
	
	public static final String[] opHelpMessage = { "/gt help #��������", "/gt use [key] #ʹ��Key", "/gt check #��ѯʣ��ʱ��",
			"/gt key [key] #���[Key]��״̬", "/gt check [name] #��ѯָ�����ʣ��ʱ��", "/gt add [name] [time] #��ָ�����������Ϸʱ��(��λ:��)"};

	public static final String[] consoleHelpMessage = { "/gt help #��������", "/gt use [key] #ʹ��Key", "/gt check #��ѯʣ��ʱ��",
			"/gt key [key] #���[Key]��״̬", "/gt check [name] #��ѯָ�����ʣ��ʱ��",
			"/gt create [value] [amount] #�½�ָ��������ָ����ֵ��key,�޺�ִ̨��",
			"/gt export <value> #��������ָ����ֵ��key,�޺�ִ̨��(����д����Ĭ�ϵ�������key)", "/gt reload #���ز�����޺�ִ̨��","/gt add [name] [time] #��ָ�����������Ϸʱ��(��λ:��)" };

	public GameTime gt;
	public HashMap<String, Long> map;

	public GTCommand(GameTime gt) {
		super(name, description, usage, aliases);
		this.gt = gt;
		map = gt.getPlayers().getPlayers();
	}

	public void help(CommandSender p) {
		if (p instanceof Player) {
			if (p.isOp())
				p.sendMessage(opHelpMessage);
			else
				p.sendMessage(playerHelpMessage);
		} else
			p.sendMessage(consoleHelpMessage);
	}

	public void refuse(CommandSender p) {
		p.sendMessage("��û��Ȩ���������");
	}

	public void tell(CommandSender p) {
		p.sendMessage("====GameTime���====");
		p.sendMessage("����: woshi85");
		p.sendMessage("�汾: " + gt.getDescription().getVersion());
	}

	public void wrong(CommandSender p) {
		p.sendMessage("��������");
	}

	public void check(CommandSender p, String name) {
		if (map.containsKey(name))
			p.sendMessage(Utils.toMessage(gt.getManager().check_message, name));
		else
			GameTime.getInstance().getCon().execute(new CheckExecutor(p, name));
	}

	public void add(CommandSender sender, String name, String value) {
		if (!Utils.isNumeric(value)) {
			sender.sendMessage("��������");
			return;
		}
		long time = Long.parseLong(value);
		if (map.containsKey(name)) {
			long l = map.get(name);
			l += time;
			map.put(name, l);
		}
		else{
			GameTime.getInstance().getCon().execute(new AddExecutor(sender,name,time));
		}
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args == null) {
			help(sender);
			return true;
		}

		final int length = args.length;
		if (length != 0 && length != 1 && length != 2 && length != 3) {
			sender.sendMessage(ChatColor.RED + "��������!");
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
			case "key":
				if (sender.hasPermission("gametime.checkkey"))
					sender.sendMessage(ChatColor.RED + "��ȷ����:/gt key [key]");
				else
					refuse(sender);
				return true;
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
					sender.sendMessage("��̨��ʹ��/gt check [���]");
				return true;
			case "export":
				if (sender instanceof Player) {
					refuse(sender);
				} else {
					GameTime.getInstance().getCon().execute(new ExportExecutor(sender));
				}
				return true;

			case "reload":
				if (sender instanceof Player)
					refuse(sender);
				else
					gt.reload();
				return true;
			default:
				sender.sendMessage(ChatColor.RED + "��������!");
				return true;
			}

		if (length == 2) {
			switch (args[0]) {
			case "use":
				if (sender instanceof Player) {
					if (sender.hasPermission("gametime.use")) {
						GameTime.getInstance().getCon().execute(new UseExecutor((Player) sender, args[1]));
					} else
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
				if (sender instanceof Player)
					if (sender.hasPermission("gametime.checkkey"))
						GameTime.getInstance().getCon().execute(new KeyExecutor(sender, args[1]));
					else
						refuse(sender);
				else
					GameTime.getInstance().getCon().execute(new KeyExecutor(sender, args[1]));
				return true;
			case "export":
				if (sender instanceof Player) {
					refuse(sender);
				} else {
					if (Utils.isNumeric(args[1]))
						GameTime.getInstance().getCon().execute(new ExportExecutor(sender, Long.parseLong(args[1])));
					else
						wrong(sender);
					return true;
				}
			default:
				sender.sendMessage(ChatColor.RED + "��������!");
				return true;
			}
		}

		if (length == 3) {
			switch (args[0]) {
			case "create":
				if (sender instanceof Player)
					refuse(sender);
				else {
					if (Utils.isNumeric(args[1]) && Utils.isNumeric(args[2]))
						GameTime.getInstance().getCon()
								.execute(new CreateExecutor(Long.parseLong(args[1]), Integer.parseInt(args[2])));
					else
						wrong(sender);
				}
				return true;
			case "add":
				if (sender instanceof Player) {
					if (GameTime.getInstance().getManager().op_can_use_add)
						if (sender.isOp())
							add(sender, args[1], args[2]);
						else
							refuse(sender);
				}
				else{
					add(sender,args[1],args[2]);
				}
				return true;
			default:
				wrong(sender);
				return true;
			}
		}
		return true;
	}
}
