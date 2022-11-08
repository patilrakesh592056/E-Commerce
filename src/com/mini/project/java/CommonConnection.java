package com.mini.project.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CommonConnection {

	public static Connection getConnection() throws SQLException {
		
		Connection connection=null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/miniporject","root","root123");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
			
		return connection;
		
		
		
		

	}

}
