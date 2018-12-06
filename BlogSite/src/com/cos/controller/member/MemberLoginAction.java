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
import com.cos.util.SHA256;

public class MemberLoginAction implements Action{
	private static String naming = "MemberLoginAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "index.jsp";
		
		MemberDAO dao = new MemberDAO();
		MemberVO member = new MemberVO();
		
		String id = request.getParameter("id");
		String salt = dao.select_salt(id);
		if(salt == null){
			Script.moving(response, "아이디가 존재하지 않습니다.");
		}
		String password = SHA256.getEncrypt(request.getParameter("password"), salt);
		member.setId(id);
		member.setPassword(password);
		
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

		int result = dao.select_id(member);
		if(result == 1){
			HttpSession session = request.getSession();
			session.setAttribute("id", member.getId());
			Script.moving(response, "로그인 성공", url);
		}else if(result == 2){
			HttpSession session = request.getSession();
			session.setAttribute("id", member.getId());
			Script.moving(response, "미인증 회원입니다. 글쓰기가 제한됩니다.", url);
		}else{
			System.out.println(naming+"sql error");
			Script.moving(response, "비밀번호를 확인하세요.");
		}
	}
}
