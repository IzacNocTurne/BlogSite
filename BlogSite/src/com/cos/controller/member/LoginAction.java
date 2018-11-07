package com.cos.controller.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.action.Action;
import com.cos.dao.MemberDAO;
import com.cos.dto.MemberVO;
import com.cos.util.Script;

public class LoginAction implements Action{
	private static String naming = "LoginAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "board?cmd=board_list";
		
		MemberVO member = new MemberVO();
		member.setId(request.getParameter("id"));
		member.setPassword(request.getParameter("password"));
		
		//쿠키저장
		if(request.getParameter("idsave") != null){
			Cookie cookie = new Cookie("cookieID",member.getId());
			cookie.setMaxAge(6000);
			response.addCookie(cookie);
		}else{
			Cookie cookie = new Cookie("cookieID",null);
			cookie.setMaxAge(0);
			response.addCookie(cookie); 
		}
		
		MemberDAO dao = new MemberDAO();
		int result = dao.select_id(member);
		if(result == 1){
			HttpSession session = request.getSession();
			session.setAttribute("id", member.getId());
			Script.moving(response, "로그인 성공", url);
		}else if(result == -1){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}
	}
}
