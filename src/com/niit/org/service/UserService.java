package com.niit.org.service;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import com.niit.org.beans.Record;
import com.niit.org.beans.User;
import com.niit.org.util.DBManager;
import com.niit.org.util.Dic;

public class UserService {
	public static List<User> queryRecord(String startDate,String endDate) throws SQLException{
		Map<String,User> userMap = new HashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn = null;
		try{
			conn = DBManager.getCon();
			Statement stmt = conn.createStatement();
			StringBuffer sb = new StringBuffer();
			sb.append("select * from user u left join ")
			.append("(select distinct user_id,`time`,record_type,comment from record")
			.append(" where 1=1 ");
			if(startDate != null){
				sb.append(" and time >= str_to_date('")
				.append(startDate)
				.append("','%Y-%m-%d')");
			}
			if(endDate != null){
				sb.append(" and time <= str_to_date('")
				.append(endDate)
				.append("','%Y-%m-%d')");
			}
			sb.append(") r on u.id = r.user_id");
			
			ResultSet rs = stmt.executeQuery(sb.toString());
			while(rs.next()){
				String no = rs.getString("no");
				if(!userMap.containsKey(no)){
					User u = new User();
					u.setId(rs.getInt("id"));
					u.setNo(no);
					u.setName(rs.getString("name"));
					u.setBatch(rs.getInt("batch"));
					u.setStuno(rs.getString("stuno"));
					userMap.put(no, u);
				}
				if(rs.getTimestamp("time") == null){
					continue;
				}
				Record r = new Record();
				r.setTime(rs.getTimestamp("time"));
				r.setRecord_type(rs.getInt("record_type"));
				r.setComment(rs.getString("comment"));
				String nowDay = sdf.format(r.getTime());
				if(!userMap.get(no).getRecords().containsKey(nowDay)){
					userMap.get(no).getRecords().put(nowDay, new ArrayList());
				}
				userMap.get(no).getRecords().get(nowDay).add(r);
			}
		}finally{
			conn.close();
		}
		List<User> userList = new ArrayList();
		for(User user:userMap.values()){
			userList.add(user);
		}
		return userList;
	}
	
	public static int applyLeave(String lsd,String led,String reason,String id)  throws SQLException{
		StringBuffer sb = new StringBuffer();
		sb.append("insert into record (user_id,time,record_type,comment) values (?,?,?,?)");
		int results[];
		Connection conn = null;
		try{
			conn = DBManager.getCon();
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Timestamp sts = Timestamp.valueOf(lsd+" 00:00:00");
			Timestamp ets = Timestamp.valueOf(led+" 00:00:00");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sts);
			while(!cal.getTime().after(ets)){
				ps.setInt(1, Integer.parseInt(id));
				ps.setTimestamp(2, Timestamp.valueOf(sdf.format(cal.getTime())));
				ps.setInt(3, Dic.recordTypes.get("请假"));
				ps.setString(4, reason);
				ps.addBatch();
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			
			results = ps.executeBatch();
			
		}finally{
			conn.close();
		}
		
		
		return results.length;
	}
	
	public static int changeName(String newName,String no) throws SQLException{
		StringBuffer sb = new StringBuffer();
		sb.append("update user set name='"+newName+"' where no='"+no+"'");
		int result = DBManager.executeUpdate(sb.toString());
		return result;
	}
	
	public static void main(String args[]) throws Exception{
		
	}
}
