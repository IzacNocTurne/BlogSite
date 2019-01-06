package com.cos.controller.board;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;
import com.cos.dao.BoardDAO;
import com.cos.dao.ReBoardDAO;
import com.cos.dto.BoardVO;
import com.cos.util.MyUtil;
import com.cos.util.Script;
import com.cos.websocket.Broadsocket;
import com.google.gson.Gson;

public class BoardDeleteAction implements Action{
	private static String naming = "BoardDeleteAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		String url = "index.jsp";
		int num = Integer.parseInt(request.getParameter("num"));
		BoardDAO dao = new BoardDAO();
		
		boolean change = false;
		ArrayList<BoardVO> hotPost1 = dao.hotpost();
		int result = dao.delete(num);
		ArrayList<BoardVO> hotPost2 = dao.hotpost();
		
		change = MyUtil.getBoardChange(hotPost1, hotPost2);
		
		if(change) {
			Gson gson = new Gson();
			String hotPostJson = gson.toJson(hotPost2);
			Broadsocket.serverMessage(hotPostJson);
		}
		
		if(result == 1){
			ReBoardDAO rdao = new ReBoardDAO();
			rdao.delete_num(num);
			Script.moving(response, "삭제 성공", url);
		}else if(result == -1){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}
	}
}
