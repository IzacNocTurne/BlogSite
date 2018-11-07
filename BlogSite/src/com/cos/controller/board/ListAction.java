package com.cos.controller.board;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;
import com.cos.dao.BoardDAO;
import com.cos.dto.BoardVO;
import com.cos.util.MyUtil;
import com.cos.util.Script;

public class ListAction implements Action{
	private static String naming = "ListAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		String url = "main.jsp";
		
		BoardDAO dao = new BoardDAO();
		ArrayList<BoardVO> list = dao.select_all();
		
		
		if(list == null){
			System.out.println(naming+"sql error");
			Script.moving(response, "홈페이지 서버 에러");
		}else{
			//main 페이지에 미리볼 수 있는 content 만들기 (이미지 제거)
			for(int i=0; i<list.size(); i++){
				list.get(i).setContent(MyUtil.preview(list.get(i).getContent()));
			}
			request.setAttribute("list", list);
			RequestDispatcher dis = request.getRequestDispatcher(url);
			dis.forward(request, response);
		}
	}
}
