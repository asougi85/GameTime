package com.shader.gt.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;


public class DBConnector implements Connector{

	private String url;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public void execute(DBExecutor exe){
		Runnable runnable = new Runnable(){
			@Override
			public void run(){
				deamon(exe);
			}
		};
		TimeQueue.push(runnable);
	}
	
	private void deamon(DBExecutor exe){
		try (Connection con = DriverManager.getConnection(url);){
			exe.run(con);
		} catch (SQLException e) {
			System.out.println("[GameTime]"+"SQLite数据库连接异常，请检查您的配置（异常代码: "+e.getErrorCode()+")");
			System.out.println("[GameTime]"+e.getLocalizedMessage());
		}
	}
	
	
	public DBConnector(String url) {
		this.url = url;
	}
	
	private Runnable deamon(CallableExecutor exe){
		try (Connection con = DriverManager.getConnection(url);){
			return exe.run(con);
		} catch (SQLException e) {
			System.out.println("[GameTime]"+"SQLite数据库连接异常，请检查您的配置（异常代码: "+e.getErrorCode()+")");
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
