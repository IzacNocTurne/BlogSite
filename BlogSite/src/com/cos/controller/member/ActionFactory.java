package com.cos.controller.member;

public class ActionFactory {
	private static ActionFactory instance = new ActionFactory();

	private ActionFactory() {
		super();
	}

	public static ActionFactory getInstance() {
		return instance;
	}
	
	public Action getAction(String cmd){
		if(cmd.equals("loginForm")) {
			return new LoginFormAction();
		}else	if(cmd.equals("joinForm")) {
			return new JoinFormAction();
		}else	if(cmd.equals("writeForm")) {
			return new WriteFormAction();
		}else	if(cmd.equals("joinAction")) {
			return new JoinAction();
		}else	if(cmd.equals("loginAction")) {
			return new LoginAction();
		}
		return null;
	}
}
