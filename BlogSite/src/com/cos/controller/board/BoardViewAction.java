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
import com.cos.util.MyUtil;
import com.cos.util.Script;
import com.cos.websocket.Broadsocket;
import com.google.gson.Gson;

public class BoardViewAction implements Action{
	private static String naming = "BoardViewAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		String url = "board/viewPage.jsp";
		
		BoardDAO dao = new BoardDAO();
		ReBoardDAO rdao = new ReBoardDAO();
	
		int num = Integer.parseInt(request.getParameter("num"));	
		
		//조회가 일어날때 웹소켓 통신 실행
		//조회가 일어나면서 조회수 top3가 변경이 되야만 실행
		//조회가 일어나기 전에 hotpost를 가져와서 1번 arraylist에 저장
		//조회 실행
		//조회 후 hotpost를 가져와서 2번 arraylist에 저장
		//두개의 arraylist 모든 값 비교 먼가 데이터가 하나라도 변경되었으면 웹소켓 통신 실행
		//글쓰기가 성공하였기 때문에 변경된 내용을 모든 사용자에게 HotPost에 알려줄 생각
		
		boolean change = false;
		ArrayList<BoardVO> hotPost1 = dao.hotpost();
		int result = dao.readcount(num);
		ArrayList<BoardVO> hotPost2 = dao.hotpost();
		
		change = MyUtil.getBoardChange(hotPost1, hotPost2);
		
		if(change) {
			Gson gson = new Gson();
			String hotPostJson = gson.toJson(hotPost2);
			Broadsocket.serverMessage(hotPostJson);
		}
		
		if(result == -1){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}
		
		BoardVO board = dao.select(num);
		
		//스크립트 공격 방어
		board.setTitle(MyUtil.getReplace(board.getTitle()));
		
		ArrayList<ReBoardVO> reboards = rdao.select_all(num);
		
		if(board == null || reboards == null){
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
