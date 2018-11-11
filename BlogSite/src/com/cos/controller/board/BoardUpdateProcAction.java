package com.cos.controller.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;
import com.cos.dao.BoardDAO;
import com.cos.dto.BoardVO;
import com.cos.util.Script;

public class BoardUpdateProcAction implements Action{
	private static String naming = "BoardUpdateAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		
		BoardVO board = new BoardVO();
		board.setNum(Integer.parseInt(request.getParameter("num")));
		board.setTitle(request.getParameter("title"));
		board.setContent(request.getParameter("content"));
		
		String url = "board?cmd=board_view&num="+board.getNum();
		
		BoardDAO dao = new BoardDAO();
		int result = dao.update(board);
		
		if(result == 1){
			Script.moving(response, "글 수정 성공", url);
		}else if(result == -1){
			System.out.println(naming+"sql error");
			Script.moving(response, "데이터베이스 에러");
		}
	}
}
