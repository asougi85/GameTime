package com.shader.gt.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.shader.gt.api.DBExecutor;

public class DebugExecutor implements DBExecutor {


	@Override
	public void run(Connection c) throws SQLException{
		try (Statement state = c.createStatement()) {
			//state.executeUpdate("DROP DATABASE db;");
			//state.executeUpdate("CREATE DATABASE db;");
			state.executeUpdate("USE db;");
			//state.executeUpdate("CREATE TABLE MAP(account tinytext,create_time integer,end_time integer,value integer);");
			//state.executeUpdate("INSERT INTO MAP(account,create_time,end_time,value) values('≤‚ ‘Key',2017,2019,5000);");
			state.executeUpdate("delete from MAP where account = '≤‚ ‘Key';");
			state.execute("SELECT * FROM MAP");
			ResultSet rs = state.getResultSet();
			if(rs==null)
				return;
			while(rs.next())
			System.out.println(rs.getString(2));
		} catch (SQLException e) {
			throw e;
		}
	}

}
