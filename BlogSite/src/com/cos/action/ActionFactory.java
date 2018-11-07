package com.cos.action;

import com.cos.controller.board.ListAction;
import com.cos.controller.board.ViewAction;
import com.cos.controller.board.WriteAction;
import com.cos.controller.member.AccountAction;
import com.cos.controller.member.JoinAction;
import com.cos.controller.member.LoginAction;
import com.cos.controller.member.LogoutAction;
import com.cos.controller.member.UpdateAction;

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
			return new ListAction();
		}else if(cmd.equals("member_join")) {
			return new JoinAction();
		}else if(cmd.equals("member_login")) {
			return new LoginAction();
		}else if(cmd.equals("member_account")){
			return new AccountAction();
		}else if(cmd.equals("member_logout")){
			return new LogoutAction();
		}else if(cmd.equals("member_update")){
			return new UpdateAction();
		}else if(cmd.equals("board_write")){
			return new WriteAction();
		}else if(cmd.equals("board_view")){
			return new ViewAction();
		}
		return null;
	}
}
