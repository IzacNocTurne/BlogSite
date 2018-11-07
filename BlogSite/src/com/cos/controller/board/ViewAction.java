package com.cos.controller.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;
import com.cos.dao.BoardDAO;
import com.cos.dto.BoardVO;
import com.cos.util.Script;

public class ViewAction implements Action{
	private static String naming = "ViewAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		String url = "board/view.jsp";
		int num = Integer.parseInt(request.getParameter("num"));
		System.out.println(naming+num);
		BoardDAO dao = new BoardDAO();
		BoardVO board = dao.select(num);
		
		if(board == null){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}else{
			request.setAttribute("board", board);
			RequestDispatcher dis = request.getRequestDispatcher(url);
			dis.forward(request, response);
		}
	}
}
