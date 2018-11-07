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

public class UpdateAction implements Action{
	private static String naming = "UpdateAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "member?cmd=account";
		
		MemberVO member = new MemberVO();
		member.setId(request.getParameter("id"));
		member.setPassword(request.getParameter("password"));
		member.setEmail(request.getParameter("email"));
		
		MemberDAO dao = new MemberDAO();
		int result = dao.update(member);
		if(result == 1){
			System.out.println(naming+"성공");
			HttpSession session = request.getSession();
			session.setAttribute("id", member.getId());
			Script.moving(response, "회원정보 수정 성공", url);
		}else if(result == -1){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}
	}
}
