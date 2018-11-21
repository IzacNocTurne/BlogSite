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

public class MemberUpdateProcAction implements Action{
	private static String naming = "MemberUpdateAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "member?cmd=member_update";
		
		MemberDAO dao = new MemberDAO();
		MemberVO member = new MemberVO();
		
		String id = request.getParameter("id");		
		String salt = dao.select_salt(id);
		String password = SHA256.getEncrypt(request.getParameter("password"), salt);
		String email = request.getParameter("email");
		
		member.setId(id);
		member.setPassword(password);
		member.setEmail(email);
		
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
