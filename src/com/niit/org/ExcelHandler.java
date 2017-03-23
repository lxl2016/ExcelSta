package com.niit.org;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.niit.org.beans.User;
import com.niit.org.util.DBManager;
import com.niit.org.util.ExcelManager;
import com.niit.org.util.FingerPrint;

/**
 * Servlet implementation class ExcelHandler
 */
@WebServlet("/ExcelHandler")
@MultipartConfig
public class ExcelHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExcelHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Part fileupload = request.getPart("fileupload");
		Map<String,String> queryDates = new HashMap();
		List<User> userList = ExcelManager.fingerPrintFile(fileupload.getInputStream(),queryDates);
		try{
			userList = FingerPrint.save(userList);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("startDate", queryDates.get("startDate"));
		request.setAttribute("endDate",  queryDates.get("endDate"));
		System.out.println("上传结束");
		response.sendRedirect("ShowData");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
