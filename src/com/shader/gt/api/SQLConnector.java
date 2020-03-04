package com.shader.gt.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import com.shader.gt.GameTime;


public class SQLConnector implements Connector{

	private String url;
	private String user;
	private String password;
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SQLConnector(String url,String user,String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	public void setUrl(String u){
		this.url = u;
	}
	
	@Override
	public void execute(DBExecutor exe) {
		Runnable runnable = new Runnable(){
			@Override
			public void run(){
				deamon(exe);
			}
		};
		TimeQueue.push(runnable);
	}
	
	private void deamon(DBExecutor exe){
		try (Connection con = DriverManager.getConnection(url, user, password);){
			exe.run(con);
		} catch (SQLException e) {
			GameTime.getInstance().getLogger().log(Level.SEVERE, "[GameTime]"+"MySQL���ݿ������쳣�������������ã��쳣����: "+e.getErrorCode()+")");
			System.out.println("[GameTime]"+e.getLocalizedMessage());
		}
	}

	private Runnable deamon(CallableExecutor exe){
		try (Connection con = DriverManager.getConnection(url, user, password);){
			return exe.run(con);
		} catch (SQLException e) {
			GameTime.getInstance().getLogger().log(Level.SEVERE, "[GameTime]"+"MySQL���ݿ������쳣�������������ã��쳣����: "+e.getErrorCode()+")");
			System.out.println("[GameTime]"+e.getLocalizedMessage());
		}
		return new Runnable(){@Override public void run(){}};
	}


	@Override
	public void execute(CallableExecutor ce) {
		Callable<Runnable> callable = new Callable<Runnable>(){
			@Override
			public Runnable call(){
				return deamon(ce);
			}
		};
		CPURunnable.push(callable);
	}
	
}
