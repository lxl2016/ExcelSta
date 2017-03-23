package com.niit.org;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import com.niit.org.beans.Record;
import com.niit.org.beans.User;
import com.niit.org.service.UserService;

/**
 * Servlet implementation class ShowData
 */
@WebServlet("/ShowData")
public class ShowData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String startDate = (String)request.getAttribute("startDate");
		String endDate = (String)request.getAttribute("endDate");
		if(request.getParameter("startDate") != null){
			startDate = request.getParameter("startDate");
		}
		if(request.getParameter("endDate") != null){
			endDate = request.getParameter("endDate");
		}
		try{
			List<User> userList = UserService.queryRecord(startDate,endDate);
			request.setAttribute("userList", userList);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("/WEB-INF/showData.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
