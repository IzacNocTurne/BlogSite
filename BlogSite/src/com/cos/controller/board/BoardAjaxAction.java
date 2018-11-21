package com.cos.controller.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;
import com.cos.dao.BoardDAO;
import com.cos.dto.BoardVO;
import com.google.gson.Gson;

public class BoardAjaxAction implements Action{
	private static String naming = "BoardAjaxAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		BoardDAO dao = new BoardDAO();
		ArrayList<BoardVO> hotPost = dao.hotpost();
		
		Gson gson = new Gson();
		String hotPostJson = gson.toJson(hotPost);
		System.out.println(naming+hotPostJson);
		PrintWriter out = response.getWriter();
		out.println(hotPostJson);
	}
}
