package com.niit.org.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private static String url="jdbc:mysql://localhost:3306/stadb?"
			+"user=videowsdemo&password=videowsdemo&characterEncoding=utf8&useSSL=false";
	
	static{
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	
	public static Connection getCon() throws SQLException{
		return DriverManager.getConnection(url);
	}
	
	//更新(update/delete/insert)
	public static int executeUpdate(String sql) throws SQLException{
		Connection conn = null;
		int result = 0;
		try{
			conn = getCon();
			Statement state = conn.createStatement();
			
			result = state.executeUpdate(sql);
		}catch(SQLException e){
			throw e;
		}finally{
			if(conn != null){
				conn.close();
			}
		}
		return result;
	}
	
	
	public static void main(String args[]){
		Connection con = null;
		try{
			con = DBManager.getCon();
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery("select * from account");
			
			while(rs.next()){
				System.out.println(rs.getInt("id"));
				System.out.println(rs.getString("username"));
				System.out.println(rs.getString("password"));
				System.out.println(rs.getString("role"));
			}
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			if(con != null){
				try{
					con.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
	
}