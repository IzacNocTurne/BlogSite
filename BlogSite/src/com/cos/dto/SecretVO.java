package com.cos.dto;

public class SecretVO {
	private String name;
	private String id;
	private String password;
	private String apikey;
	private String apisecret;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	public String getApisecret() {
		return apisecret;
	}
	public void setApisecret(String apisecret) {
		this.apisecret = apisecret;
	}
}
