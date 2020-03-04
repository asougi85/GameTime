package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;

import com.shader.gt.GameTime;
import com.shader.gt.Utils;
import com.shader.gt.api.CallableExecutor;

public class CheckExecutor implements CallableExecutor {

	private String user;
	private CommandSender sender;
	private String message = "";
	public CheckExecutor(CommandSender sender,String user) {
		this.sender = sender;
		this.user = user;
		
	}

	@Override
	public Runnable run(Connection c) throws SQLException {		
		try (PreparedStatement state = c.prepareStatement("SELECT * FROM TIME WHERE user = ?")) {
			state.setString(1, user);
			state.execute();
			ResultSet rs = state.getResultSet();
			
			if(rs == null)
				message = Utils.toMessage(GameTime.getInstance().getManager().check_missing_message);
			else
				if(rs.next()){
					long time = rs.getLong(2);
					message = Utils.toMessage(GameTime.getInstance().getManager().check_message,user,time);
				}
				else
					message = Utils.toMessage(GameTime.getInstance().getManager().check_missing_message);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return new Runnable(){
			public void run(){
				sender.sendMessage(message);
			}
		};
	}

}
