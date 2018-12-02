package com.cos.dto;

public class SMSVO {
	private String id;
	private String msg;
	private String reply;
	private String writedate;
	private boolean replycheck;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getWritedate() {
		return writedate;
	}
	public void setWritedate(String writedate) {
		this.writedate = writedate;
	}
	public boolean isReplycheck() {
		return replycheck;
	}
	public void setReplycheck(boolean replycheck) {
		this.replycheck = replycheck;
	}
	
	
}
