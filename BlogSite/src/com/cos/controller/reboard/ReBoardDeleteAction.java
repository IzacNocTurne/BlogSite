package com.cos.controller.reboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cos.action.Action;
import com.cos.dao.ReBoardDAO;
import com.cos.util.Script;

public class ReBoardDeleteAction implements Action{
	private static String naming = "ReBoardDeleteAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(naming);
		ReBoardDAO dao = new ReBoardDAO();
		
		int num = Integer.parseInt(request.getParameter("num"));
		int renum = Integer.parseInt(request.getParameter("renum"));
		String url = "board?cmd=board_view&num="+num;
		
		int result = dao.delete(renum);
		
		if(result == 1){
			Script.moving(response, "댓글 삭제됨", url);
		}else{
			Script.moving(response, "database 에러");
		}
	}
}
