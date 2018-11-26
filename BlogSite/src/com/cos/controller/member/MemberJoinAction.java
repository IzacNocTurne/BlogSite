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
import com.cos.util.SHA256;

public class MemberJoinAction implements Action{
	private static String naming = "MemberJoinAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "gmail/emailSendAction.jsp";
		
		String salt = SHA256.generateSalt();;
		String password = SHA256.getEncrypt(request.getParameter("password"), salt);
		
		MemberVO member = new MemberVO();
		member.setId(request.getParameter("id"));
		member.setPassword(password);
		member.setUsername(request.getParameter("username"));
		member.setEmail(request.getParameter("email"));
		member.setSalt(salt);
		
		
		MemberDAO dao = new MemberDAO();
		int result = dao.insert(member);
		if(result == 1){
			HttpSession session = request.getSession();
			session.setAttribute("id", member.getId());
			Script.moving(response, "회원가입 성공 이메일 인증을 진행해주세요.", url);
		}else if(result == -1){
			System.out.println(naming+"sql error");
			Script.moving(response, "database 에러");
		}
	}
}
