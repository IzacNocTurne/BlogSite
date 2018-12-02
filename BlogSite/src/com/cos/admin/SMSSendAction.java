package com.cos.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.action.Action;
import com.cos.dao.AdminDAO;
import com.cos.dao.SecretDAO;
import com.cos.dto.SMSVO;
import com.cos.dto.SecretVO;
import com.cos.util.Coolsms;

public class SMSSendAction implements Action{
	private static String naming = "SendSMSAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AdminDAO dao = new AdminDAO();
		HttpSession session = request.getSession();
		
		String id = null;
		if(session.getAttribute("id") != null){
			id = (String)session.getAttribute("id");
		}
		
		String msg = "";
		BufferedReader in = request.getReader();
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line = in.readLine()) != null) {
			sb.append(line);
		}
		System.out.println(naming+sb.toString());
		msg = sb.toString();
		
		int result = 0;
/*		if(id != null){
			result = Coolsms.sendSMS(msg);	
		}*/
		
		if(id != null){
			if(id.equals("admin")) result = Coolsms.sendSMS(msg);	
		}
		
		PrintWriter out = response.getWriter();
		if(result == 1){
			SMSVO sms = new SMSVO();
			sms.setId(id);
			sms.setMsg(msg);
			dao.insert_sms(sms);
			
			out.println("SMS전송 성공");
			
		}else if(result == -1){
			out.println("SMS전송 실패");
		}else if(result == 0){
			/*out.println("로그인을 먼저 진행하세요.");*/
			out.println("테스트 서버입니다. 관리자 아이디만 sms전송이 가능합니다.");
		}
	}
}
