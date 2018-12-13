package com.cos.controller.board;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;

import com.cos.action.Action;
import com.cos.dao.BoardDAO;
import com.cos.dao.ReBoardDAO;
import com.cos.dto.BoardVO;
import com.cos.dto.ReBoardVO;
import com.cos.util.MyUtil;
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
		
		//Jsoup.
		
		ArrayList<ReBoardVO> reboards = rdao.select_all(num);
		
		if(board == null){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}else{
			request.setAttribute("board", board);
			
			//유튜브 영상 걸러내기
			board.setContent(MyUtil.makeYoutube(board.getContent()));
			
			//네이버 지도 걸러내기 규칙은 /nmap/중앙대로 708/nmap/ 형태로 데이터 입력해야함.
			board.setContent(MyUtil.makeNavermap(board.getContent()));
			
			request.setAttribute("reboards", reboards);
			RequestDispatcher dis = request.getRequestDispatcher(url);
			dis.forward(request, response);
		}
	}
}
