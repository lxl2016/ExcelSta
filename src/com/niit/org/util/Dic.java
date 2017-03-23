package com.niit.org.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class Dic {
	//单例模式
	public static Map<String,Integer> batches = new HashMap();
	public static Map<Integer,String> batchesById = new HashMap();
	public static Map<String,Integer> recordTypes = new HashMap();
	
	static{
		Connection conn = null;
		try{
			conn = DBManager.getCon();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from batch");
			
			while(rs.next()){
				batches.put(rs.getString("name"),rs.getInt("id"));
				batchesById.put(rs.getInt("id"),rs.getString("name"));
			}
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from record_type");
			while(rs.next()){
				recordTypes.put(rs.getString("record_name"),rs.getInt("id"));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}