package com.cos.action;

import com.cos.controller.board.BoardAjaxAction;
import com.cos.controller.board.BoardDeleteAction;
import com.cos.controller.board.BoardListAction;
import com.cos.controller.board.BoardSearchAction;
import com.cos.controller.board.BoardUpdateAction;
import com.cos.controller.board.BoardUpdateProcAction;
import com.cos.controller.board.BoardViewAction;
import com.cos.controller.board.BoardWriteAction;
import com.cos.controller.member.MemberJoinAction;
import com.cos.controller.member.MemberLoginAction;
import com.cos.controller.member.MemberLogoutAction;
import com.cos.controller.member.MemberUpdateAction;
import com.cos.controller.member.MemberUpdateProcAction;
import com.cos.controller.reboard.ReBoardDeleteAction;
import com.cos.controller.reboard.ReBoardReplyAction;

public class ActionFactory {
	private static ActionFactory instance = new ActionFactory();

	private ActionFactory() {
		super();
	}

	public static ActionFactory getInstance() {
		return instance;
	}
	
	public Action getAction(String cmd){
		if(cmd.equals("board_list")){
			return new BoardListAction();
		}else if(cmd.equals("member_join")) {
			return new MemberJoinAction();
		}else if(cmd.equals("member_login")) {
			return new MemberLoginAction();
		}else if(cmd.equals("member_update")){
			return new MemberUpdateAction();
		}else if(cmd.equals("member_logout")){
			return new MemberLogoutAction();
		}else if(cmd.equals("member_updateProc")){
			return new MemberUpdateProcAction();
		}else if(cmd.equals("board_write")){
			return new BoardWriteAction();
		}else if(cmd.equals("board_view")){
			return new BoardViewAction();
		}else if(cmd.equals("board_delete")){
			return new BoardDeleteAction();
		}else if(cmd.equals("board_update")){
			return new BoardUpdateAction();
		}else if(cmd.equals("board_updateProc")){
			return new BoardUpdateProcAction();
		}else if(cmd.equals("board_ajax")){
			return new BoardAjaxAction(); 
		}else if(cmd.equals("board_search")){
			return new BoardSearchAction();
		}else if(cmd.equals("reboard_reply")){
			return new ReBoardReplyAction();
		}else if(cmd.equals("reboard_delete")){
			return new ReBoardDeleteAction();
		}		
		return null;
	}
}
