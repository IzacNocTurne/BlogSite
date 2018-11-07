package com.cos.controller.member;

import java.io.IOException;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.cos.action.Action;

public class LogoutAction implements Action{
	private static String naming = "LogoutAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		String url = "board?cmd=board_list";
		
		HttpSession session = request.getSession();
		session.invalidate();
		
		response.sendRedirect(url);
	}
}
