package com.niit.org.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import com.niit.org.beans.Record;
import com.niit.org.beans.User;

public class FingerPrint {
	public static List<User> save(List<User> userList) throws SQLException{
		for(User user:userList){
			Connection conn = null;
			try{
				conn = DBManager.getCon();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select 1 from user where `no`='"+user.getNo()+"'");
				if(!rs.next()){
					//新用户，创建
					StringBuffer sb = new StringBuffer();
					sb.append("insert into user(`no`,`name`,`batch`) values ('")
						.append(user.getNo())
						.append("','")
						.append(user.getName())
						.append("',")
						.append(user.getBatch())
						.append(")");
					DBManager.executeUpdate(sb.toString());
				}
				
				//获取用户
				StringBuffer sb = new StringBuffer()
						.append("select id from user where `no`='"+user.getNo()+"'");
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sb.toString());
				if(rs.next()){
					user.setId(rs.getInt("id"));
				}else{
					conn.close();
					throw new SQLException("用户信息出错!");
				}
				
				
				PreparedStatement ps = conn.prepareStatement("insert into record (user_id,time,record_type) values(?,?,?)");

				//记录
				if(user.getRecords().size() != 0){
					for(List<Record> records:user.getRecords().values()){
						for(Record record:records){
							ps.setInt(1,user.getId());
							ps.setTimestamp(2, record.getTime());
							ps.setInt(3,record.getRecord_type());
							ps.addBatch();
						}
					}
					
					ps.executeBatch();
				}
			}finally{
				conn.close();
			}
		}
		
		return userList;
	}
}
