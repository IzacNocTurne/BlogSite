package com.cos.controller.board;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.action.Action;
import com.cos.dao.BoardDAO;
import com.cos.dao.MemberDAO;
import com.cos.dto.BoardVO;
import com.cos.util.Script;
import com.cos.websocket.Broadsocket;
import com.google.gson.Gson;

public class BoardWriteAction implements Action{
	private static String naming = "BoardWriteAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		String url = "index.jsp";
		
		BoardVO board = new BoardVO();
		MemberDAO mdao = new MemberDAO();
		
		//JSP경로설정 이해 
		//.java파일에서는 경로의 시작이 ContextPath/여기부터이다.
		//.jsp파일에서는 경로의 시작이 현재 폴더 기준이다. 
		String id = null;
		HttpSession session = request.getSession();
		if(session.getAttribute("id") != null){
			id = (String)session.getAttribute("id");
			int result = mdao.select_emailcheck(id);
			if(result != 1){
				Script.moving(response, "먼저 인증을 진행해주세요.", "member?cmd=member_update");
			}else{
				board.setId(request.getParameter("id"));
				board.setTitle(request.getParameter("title"));
				board.setContent(request.getParameter("content"));
				
				BoardDAO dao = new BoardDAO();
				int insert = dao.insert(board);
				if(insert == 1){
					Script.moving(response, "글쓰기 성공", url);
				}else if(insert == -1){
					System.out.println(naming+"sql error");
					Script.moving(response, "database 에러");
				}
			}
		}else{
			Script.moving(response, "먼저 로그인을 진행해주세요.", "member/loginForm.jsp");
		}	
	}
}
