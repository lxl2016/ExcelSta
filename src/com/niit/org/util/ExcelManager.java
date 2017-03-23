package com.niit.org.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.niit.org.beans.Record;
import com.niit.org.beans.User;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

public class ExcelManager {
//	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	private static HSSFWorkbook readFile(InputStream is) throws IOException{
		try{
			return new HSSFWorkbook(is);
		}finally{
			is.close();
		}
	}
	
	
	public static List<User> fingerPrintFile(InputStream is,Map<String,String> queryDates) throws IOException{
		HSSFWorkbook wb = readFile(is);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//统计信息
		List<User> userList = new ArrayList();
		for(int k=0;k<wb.getNumberOfSheets();k++){
			HSSFSheet sheet = wb.getSheetAt(k);
			//只统计考勤记录
			if(!sheet.getSheetName().equals("考勤记录")){
				continue;
			}
			//开始/结束时间
			String startDate = "",endDate = "";
			Date sd=null,ed=null;
			//制表时间
			String staTime = null;
			
			int rows = sheet.getPhysicalNumberOfRows();
			User newUser = null;
			for(int r=0; r<rows; r++){
				HSSFRow row = sheet.getRow(r);
				if(row == null){
					continue;
				}
				//从第五行起，r偶数时有用户
				if(r>3 && r%2 == 0){
					newUser = new User();
				}
				
				int cells = row.getPhysicalNumberOfCells();
				//处理各种特殊单元
				if(r==2){
					//循环遍历
					for(int c=0; c<cells; c++){
						HSSFCell cell = row.getCell(c);
						String value = getCellValue(cell);
						//考勤月份
						if("考勤时间".equals(value)){
							String staTimes = getCellValue(row.getCell(c+2));
							startDate = staTimes.substring(0,10);
							endDate = staTimes.substring(13);
							queryDates.put("startDate", startDate);
							queryDates.put("endDate", endDate);
							try{
								sd = sdf.parse(startDate);
								ed = sdf.parse(endDate);
							} catch (java.text.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						//制表时间
						if("制表时间".equals(value)){
							staTime = getCellValue(row.getCell(c+2));
						}
					}
					
				}
				//工号
				if(r>3 && r%2 == 0){
					//循环遍历
					for(int c=0; c<cells; c++){
						HSSFCell cell = row.getCell(c);
						String value = getCellValue(cell);
						//用户工号
						if("工 号:".equals(value)){
							newUser.setNo(getCellValue(row.getCell(c+2)));
						}
						//姓名
						if("姓 名:".equals(value)){
							newUser.setName(getCellValue(row.getCell(c+2)));
						}
						//部 门
						if("部 门:".equals(value)){
							int id = 0;
							String batch = getCellValue(row.getCell(c+2));
							if(Dic.batches.containsKey(batch.toLowerCase())){
								id = Dic.batches.get(batch.toLowerCase());
							}
							newUser.setBatch(id);
						}
						
					}
				}
				//出勤
				if(r>4 && r%2 == 1){
					//循环遍历
					for(int c=0; c<cells; c++){
						HSSFCell cell = row.getCell(c);
						String value = getCellValue(cell);
						if(value != null && !"".equals(value)){
							Calendar cl = Calendar.getInstance();
							cl.setTime(sd);
							cl.add(Calendar.DAY_OF_MONTH, c);
							//解析出勤时间
							for(int i=0;i<value.length();i=i+5){
								String time = value.substring(i, i+5);
								String nowDay = sdf.format(cl.getTime());
								StringBuffer tmpSb = new StringBuffer(nowDay);
								tmpSb.append(" ").append(time).append(":00");
								Record record = new Record();
								record.setRecord_type(Dic.recordTypes.get("指纹打卡机"));
								record.setTime(Timestamp.valueOf(tmpSb.toString()));
								
								if(!newUser.getRecords().containsKey(nowDay)){
									newUser.getRecords().put(nowDay, new ArrayList());
								}
								newUser.getRecords().get(nowDay).add(record);
							}
						}
					}
					//读取完数据后，如果不为空，添加到列表
					if(newUser != null){
						userList.add(newUser);
					}
				}
			}
		}
		wb.close();
		return userList;
	}
	
	private static String getCellValue(HSSFCell cell){
		String value = null;
		switch (cell.getCellTypeEnum()) {
			case FORMULA:
				value = cell.getCellFormula().toString();
				break;
			case NUMERIC:
				value = String.valueOf(cell.getNumericCellValue());
				break;
			case STRING:
				value = cell.getStringCellValue();
				break;
			default:
		}
		return value;
	}
	
	public static void main(String args[]) throws Exception{
		
		
		
		HSSFWorkbook wb = readFile(new FileInputStream(new File("C:/Users/xl/Desktop/dlt.xls")));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		HSSFSheet sheet = wb.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		String url = "jdbc:mysql://localhost:3306/lottery?"
				+"user=videowsdemo&password=videowsdemo&characterEncoding=utf8&useSSL=false";
		Connection conn = null;
		try{
			conn = DriverManager.getConnection(url);
			StringBuffer sb = new StringBuffer();
			sb.append("insert into prizes values ")
				.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement ps = conn.prepareStatement(sb.toString());
		
			for(int i=2;i<rows;i++){
				HSSFRow row = sheet.getRow(i);
				//期号
				HSSFCell cell = row.getCell(0);
				String qihao = getCellValue(cell);
				ps.setLong(1, Math.round(Double.parseDouble(qihao)));
				//前区
				cell = row.getCell(1);
				String prefix1 = getCellValue(cell);
				String tmpstr = "";
				if(Math.round(Double.parseDouble(prefix1)) < 10){
					tmpstr += "0";
				}
				tmpstr += Math.round(Double.parseDouble(prefix1));
				ps.setString(2, tmpstr);
				
				cell = row.getCell(2);
				String prefix2 = getCellValue(cell);
				tmpstr = "";
				if(Math.round(Double.parseDouble(prefix2)) < 10){
					tmpstr += "0";
				}
				tmpstr += Math.round(Double.parseDouble(prefix2));
				ps.setString(3, tmpstr);
				
				cell = row.getCell(3);
				String prefix3 = getCellValue(cell);
				tmpstr = "";
				if(Math.round(Double.parseDouble(prefix3)) < 10){
					tmpstr += "0";
				}
				tmpstr += Math.round(Double.parseDouble(prefix3));
				ps.setString(4, tmpstr);
				
				cell = row.getCell(4);
				String prefix4 = getCellValue(cell);
				tmpstr = "";
				if(Math.round(Double.parseDouble(prefix4)) < 10){
					tmpstr += "0";
				}
				tmpstr += Math.round(Double.parseDouble(prefix4));
				ps.setString(5, tmpstr);
				
				cell = row.getCell(5);
				String prefix5 = getCellValue(cell);
				tmpstr = "";
				if(Math.round(Double.parseDouble(prefix5)) < 10){
					tmpstr += "0";
				}
				tmpstr += Math.round(Double.parseDouble(prefix5));
				ps.setString(6, tmpstr);
				
				//后区
				cell = row.getCell(6);
				String append1 = getCellValue(cell);
				tmpstr = "";
				if(Math.round(Double.parseDouble(append1)) < 10){
					tmpstr += "0";
				}
				tmpstr += Math.round(Double.parseDouble(append1));
				ps.setString(7, tmpstr);
				
				cell = row.getCell(7);
				String append2 = getCellValue(cell);
				tmpstr = "";
				if(Math.round(Double.parseDouble(append2)) < 10){
					tmpstr += "0";
				}
				tmpstr += Math.round(Double.parseDouble(append2));
				ps.setString(8, tmpstr);
				
				//奖池奖金
				cell = row.getCell(8);
				String moneyinpound = getCellValue(cell);
				ps.setLong(9, Math.round(Double.parseDouble(moneyinpound)));
				//一等奖人数
				cell = row.getCell(9);
				String firstprizecount = getCellValue(cell);
				ps.setLong(10, Math.round(Double.parseDouble(firstprizecount)));
				//一等奖钱数
				cell = row.getCell(10);
				String firstprize = getCellValue(cell);
				ps.setLong(11, Math.round(Double.parseDouble(firstprize)));
				//二等奖人数
				cell = row.getCell(11);
				String secondprizecount = getCellValue(cell);
				ps.setLong(12, Math.round(Double.parseDouble(secondprizecount)));
				//二等奖钱数
				cell = row.getCell(12);
				String secondprize = getCellValue(cell);
				ps.setLong(13, Math.round(Double.parseDouble(secondprize)));
				//总投注额
				cell = row.getCell(13);
				String summarybet = getCellValue(cell);
				ps.setLong(14, Math.round(Double.parseDouble(summarybet)));
				//开奖日期
				cell = row.getCell(14);
				String prizedate = getCellValue(cell);
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse("1899-12-30"));
				c.add(Calendar.DAY_OF_MONTH, (int)Math.round(Double.parseDouble(prizedate)));
				ps.setDate(15, java.sql.Date.valueOf(sdf.format(c.getTime())));
				
				ps.addBatch();
			}
			ps.executeBatch();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			conn.close();
		}
	}
}
