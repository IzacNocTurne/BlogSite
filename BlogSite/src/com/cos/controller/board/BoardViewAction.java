package com.cos.controller.board;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;
import com.cos.dao.BoardDAO;
import com.cos.dao.ReBoardDAO;
import com.cos.dto.BoardVO;
import com.cos.dto.ReBoardVO;
import com.cos.util.Script;

public class BoardViewAction implements Action{
	private static String naming = "BoardViewAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		String url = "board/viewPage.jsp";
		
		BoardDAO dao = new BoardDAO();
		ReBoardDAO rdao = new ReBoardDAO();
	
		int num = Integer.parseInt(request.getParameter("num"));	
		int result = dao.readcount(num);
		if(result == -1){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}
		
		BoardVO board = dao.select(num);
		ArrayList<ReBoardVO> reboards = rdao.select_all(num);
		
		if(board == null){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}else{
			request.setAttribute("board", board);
			request.setAttribute("reboards", reboards);
			RequestDispatcher dis = request.getRequestDispatcher(url);
			dis.forward(request, response);
		}
	}
}
