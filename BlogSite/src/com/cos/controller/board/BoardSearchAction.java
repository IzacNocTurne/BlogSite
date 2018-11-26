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

public class BoardSearchAction implements Action{
	private static String naming = "BoardSearchAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		String url = "main.jsp";
		
		String search = "";
		if(request.getParameter("search") != null){
			search = request.getParameter("search");
		}
		
		BoardDAO dao = new BoardDAO();
		ArrayList<BoardVO> list = dao.search();
		ArrayList<BoardVO> result = new ArrayList<>();
		
		//검색(title은 그냥 검색, content는 html태그 제거 후 contains함수를 이용한 문자 검색)
		for(int i=0; i< list.size(); i++){
			String html_content = list.get(i).getContent();
			String title = list.get(i).getTitle();
			String content = MyUtil.removeTag(html_content);
			
			if(content.contains(search) || title.contains(search)){
				result.add(list.get(i));
			}
		}
		
		ArrayList<BoardVO> hotPost = dao.hotpost();
		
		//main 페이지에 미리볼 수 있는 content 만들기 (이미지 제거)
		for(int i=0; i<result.size(); i++){
			result.get(i).setContent(MyUtil.preview(result.get(i).getContent()));
		}
		
		request.setAttribute("search", true);
		request.setAttribute("list", result);
		request.setAttribute("hotPost", hotPost);
		
		RequestDispatcher dis = request.getRequestDispatcher(url);
		dis.forward(request, response);
	}
	
}
