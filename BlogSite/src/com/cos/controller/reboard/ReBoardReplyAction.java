package com.cos.controller.reboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.cos.action.Action;
import com.cos.dao.ReBoardDAO;
import com.cos.dto.ReBoardVO;
import com.google.gson.Gson;

public class ReBoardReplyAction implements Action{
	private static String naming = "ReBoardReplyAction : ";
	@SuppressWarnings("unchecked")
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		
		//Ajax통신으로 넘어온 데이터 받기
		BufferedReader in = request.getReader();
		StringBuffer sb = new StringBuffer();
		String line = null;
		while((line = in.readLine()) != null) {
			sb.append(line);
		}
		System.out.println(naming+sb.toString());
		
		//recontent, id, num 세가지를 받는다. wirtedate는 db에 넣을 때 넣어준다. 
		JSONParser parser = new JSONParser();
		JSONObject reboardJson = null;
		try {
			reboardJson = (JSONObject)parser.parse(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//gson을 이용해서 beans에 담아준다.
		Gson gson = new Gson();
		ReBoardVO reboard1 = gson.fromJson(reboardJson.toString(), ReBoardVO.class);
		
		//DB저장
		ReBoardDAO rdao = new ReBoardDAO();
		int auto_increment = rdao.select_renum();
		reboard1.setRenum(auto_increment+1);
		int result = rdao.insert(reboard1);
		
		reboardJson.put("renum", auto_increment+1);
		
		//결과 응답해주기
		if(result == 1){
			PrintWriter out = response.getWriter();
			out.println(reboardJson);
		}
	}
}
