package com.cos.controller.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;
import com.cos.action.ActionFactory;

@WebServlet("/board")
public class BoardContoller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private static final String naming = "BoardContoller : ";
       
    public BoardContoller() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//System.out.println(naming+"GET");
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//System.out.println(naming+"POST");
		doProcess(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8"); 
		String cmd = null;
		if(request.getParameter("cmd") != null){
			cmd = request.getParameter("cmd");
		}
		
		//System.out.println(naming+cmd);
		ActionFactory af=ActionFactory.getInstance();
		Action action=af.getAction(cmd);
		if(action != null) action.execute(request, response);
	}	

}
