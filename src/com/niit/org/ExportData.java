package com.niit.org;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.niit.org.beans.Record;
import com.niit.org.beans.User;
import com.niit.org.service.UserService;
import com.niit.org.util.Dic;

/**
 * Servlet implementation class ExportData
 */
@WebServlet("/ExportData")
public class ExportData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String startDate = (String)request.getAttribute("startDate");
		String endDate = (String)request.getAttribute("endDate");
		if(request.getParameter("startDate") != null){
			startDate = request.getParameter("startDate");
		}
		if(request.getParameter("endDate") != null){
			endDate = request.getParameter("endDate");
		}
		List<User> userList = null;
		try{
			userList = UserService.queryRecord(startDate,endDate);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		if(userList == null){
			return;
		}
		
		//excle文件

		String path = this.getServletContext().getRealPath("/");
		String tmp = path+"/tmp";
		File f = new File(tmp);
		if(!f.exists()){
			f.mkdir();
		}
		String fileName = System.currentTimeMillis()+".xls";
		path = tmp+"/"+fileName;
		
		Workbook wb = new HSSFWorkbook();
		Sheet sheet1 = wb.createSheet("出勤统计");
		
		//所有表格样式
		CellStyle defaultstyle = wb.createCellStyle();
		defaultstyle.setBorderBottom(BorderStyle.THIN);
		defaultstyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		defaultstyle.setBorderLeft(BorderStyle.THIN);
		defaultstyle.setLeftBorderColor(IndexedColors.GREEN.getIndex());
		defaultstyle.setBorderRight(BorderStyle.THIN);
		defaultstyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
		defaultstyle.setBorderTop(BorderStyle.THIN);
		defaultstyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		//rowstyle -- 单行间隔行颜色
		CellStyle rowstyle = wb.createCellStyle();
		rowstyle.cloneStyleFrom(defaultstyle);
		rowstyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
		rowstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		//titlestyle -- 标题行颜色
		CellStyle titlestyle = wb.createCellStyle();
		titlestyle.cloneStyleFrom(defaultstyle);
		Font font = wb.createFont();
        font.setBold(true);
        titlestyle.setFont(font);
        
        
        
        
     
        
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sd = new Date();
		Date ed = new Date();
		try {
			sd = sdf.parse(startDate);
			ed = sdf.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar scl = Calendar.getInstance();
		scl.setTime(sd);
		
		int rowindex = 0;
        //标题行
		Row row0 = sheet1.createRow(rowindex);
		int i = 0;
		Cell cell = row0.createCell(i);
		cell.setCellValue("工号");
		cell.setCellStyle(titlestyle);
		i++;
		cell = row0.createCell(i);
		cell.setCellValue("姓名");
		cell.setCellStyle(titlestyle);
		i++;
		cell = row0.createCell(i);
		cell.setCellValue("部门");
		cell.setCellStyle(titlestyle);
		
		sdf.applyPattern("MM-dd");
		while(!scl.getTime().after(ed)){
			i++;
			cell = row0.createCell(i);
			cell.setCellValue(sdf.format(scl.getTime()));
			cell.setCellStyle(titlestyle);
			scl.add(Calendar.DAY_OF_MONTH,1);
		}
		
		Collections.sort(userList);
		//数据行
		sdf.applyPattern("yyyy-MM-dd");
		SimpleDateFormat sdfhm = new SimpleDateFormat("HH:mm");
		for(User user:userList){ 
			rowindex++;
			CellStyle cstyle = null;
			//偶数行有颜色
			if(rowindex%2 == 0){
				cstyle = rowstyle;
			}else{
				cstyle = defaultstyle;
			}
			scl.setTime(sd);
			Row row = sheet1.createRow(rowindex);
			i = 0;
			cell = row.createCell(i);
			cell.setCellValue(user.getNo());
			cell.setCellStyle(cstyle);
			i++;
			cell = row.createCell(i);
			cell.setCellValue(user.getName());
			cell.setCellStyle(cstyle);
			i++;
			cell = row.createCell(i);
			cell.setCellValue(Dic.batchesById.get(user.getBatch()));
			cell.setCellStyle(cstyle);
			
			while(!scl.getTime().after(ed)){
				i++;
				cell = row.createCell(i);
				cell.setCellStyle(cstyle);
				String nowDay = sdf.format(scl.getTime());
				StringBuffer sb = new StringBuffer();
				if(user.getRecords().containsKey(nowDay)){ 
					Collections.sort(user.getRecords().get(nowDay));
					for(Record record:user.getRecords().get(nowDay)){
						if(Dic.recordTypes.get("请假")==record.getRecord_type()){
							sb = new StringBuffer("请假：").append(record.getComment());
							break;
						}else{
							sb.append(sdfhm.format(record.getTime())).append("  ");
						}
					}
				}
				cell.setCellValue(sb.toString());
				scl.add(Calendar.DAY_OF_MONTH,1);
			}
		}
		
		
	    FileOutputStream fileOut = new FileOutputStream(path);
	    wb.write(fileOut);
	    fileOut.close();
	    
	    //--------------------------------------------------------------------------------------------
		
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition","attachment;filename=\""+fileName+"\"");
		InputStream is = null;
		OutputStream os = null;
		try{
			
			is = new BufferedInputStream(new FileInputStream(path));
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			os = new BufferedOutputStream(response.getOutputStream());
			
			byte[] buffer = new byte[4*1024];
			int read = 0;
			
			while((read=is.read(buffer)) != -1){
				baos.write(buffer,0,read);
			}
			
			os.write(baos.toByteArray());
			
		}finally{
			os.close();
			is.close();
		}
		
		File outFile = new File(path);
		outFile.delete();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
