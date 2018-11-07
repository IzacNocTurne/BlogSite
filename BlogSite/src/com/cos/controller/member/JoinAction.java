package com.cos.controller.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.action.Action;
import com.cos.dao.MemberDAO;
import com.cos.dto.MemberVO;
import com.cos.util.Script;

public class JoinAction implements Action{
	private static String naming = "JoinAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "/BlogSite/main.jsp";
		
		MemberVO vo = new MemberVO();
		vo.setId(request.getParameter("id"));
		vo.setPassword(request.getParameter("password"));
		vo.setUsername(request.getParameter("username"));
		vo.setEmail(request.getParameter("email"));
		
		MemberDAO dao = new MemberDAO();
		int result = dao.insert(vo);
		if(result == 1){
			HttpSession session = request.getSession();
			session.setAttribute("id", vo.getId());
			Script.moving(response, "회원가입 성공", url);
		}else if(result == -1){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}
	}
}
