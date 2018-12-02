package com.cos.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;


/*
 *	SMS를 받으면 보낸 유저의 id를 알 수 있다. 그 아이디로 member 테이블의 전화번호를 확인한다.
 * 그리고 sms replycheck 여부를 확인하여 reply가 되지 않은 답변들을 관리자 페이지에서 확인한다.
 * 관리자 페이지에서 유저 전화번호로 답변을 한다. 답변이 완료되면 sms테이블의 replycheck여부를 true로 변경한다.
 */
public class SMSReplyAction implements Action{
	private static String naming = "SMSReplyAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
