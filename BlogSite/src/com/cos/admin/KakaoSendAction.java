package com.cos.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;
import com.cos.dao.AdminDAO;
import com.cos.util.Coolsms;

/*카카오 메시지를 받으면 일단 응답을 DB에서 count해서 응답 개수가 1건에 3분정도 걸린다고 치고 응답시간을 설정한다.
예를 들어 DB에 응답하지 않은 카카오 메시지가 20건이면 답변은 1시간 후에 가능합니다라고 일단 응답한다.
응답 후 문의 내용을 내 DB에 저장시킨다. DB는 API테스트 후 만들자. 왜냐하면 1:1채팅 메시지에 메시지 내용말고 
해당 메시지가 누구한테 왔는지에 대한 key값을 ID와 매칭 시켜서 DB에 저장해야하는데 key값 확인을 아직 안해봄.*/

public class KakaoSendAction implements Action{
	private static String naming = "KakaoSendAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msg = "";
		BufferedReader in = request.getReader();
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line = in.readLine()) != null) {
			sb.append(line);
		}
		System.out.println(naming+sb.toString());
		
			

	}
}
