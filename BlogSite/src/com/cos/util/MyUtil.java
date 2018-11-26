package com.cos.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MyUtil {
	private static String naming = "MyUtil : ";
	public static String getMyCookie(HttpServletRequest request) {
		String cookieID = null;

		Cookie[] cookies = request.getCookies();

		for (Cookie c : cookies) {
			if (c.getName().equals("cookieID")) {
				// 엘레멘트 찾아서 넣어주면 됨.
				cookieID = c.getValue();
			}
		}
		System.out.println(naming+cookieID);
		return cookieID;
	}
	
	public static String getReplace(String code) {	
		return code.replaceAll(" ", "&nbsp;").replaceAll("<", "&lt").replaceAll(">", "&gt").replaceAll("\n", "<br>");
	}
	
	public static String preview(String content){
		String result = "";
		
		//p_tag
		String p_value = "";
		int p_length = 0;
		int p_index = 0;
		boolean p_vacant = true;
		
		//div_tag
		String div_value = "";
		int div_length = 0;
		int div_index = 0;
		boolean div_vacant = true;
		
		//span_tag
		String span_value = "";
		int span_length = 0;
		int span_index = 0;
		boolean span_vacant = true;
		
		//img_tag
		boolean img_vacant = true;
		
		Document doc = Jsoup.parse(content);
		Elements p_tag = doc.select("p");	
		Elements div_tag = doc.select("div");
		Elements span_tag = doc.select("span");
		Elements img_tag = doc.select("img");
		
		for(int i=0; i<p_tag.size(); i++){
			p_vacant = false;
			p_value = doc.select("p").eq(i).text();
			if(p_length < p_value.length()){
				p_length = p_value.length();
				if(!p_value.equals("")){
					p_index = i;
				}
			}
		}
		
		for(int i=0; i<div_tag.size(); i++){
			div_vacant = false;
			div_value = doc.select("div").eq(i).text();
			if(div_length < div_value.length()){
				div_length = div_value.length();
				if(!div_value.equals("")){
					div_index = i;
				}
			}
		}
		
		for(int i=0; i<span_tag.size(); i++){
			span_vacant = false;
			span_value = doc.select("span").eq(i).text();
			if(span_length < span_value.length()){
				span_length = span_value.length();
				if(!span_value.equals("")){
					span_index = i;
				}
			}
		}
		
		if(img_tag.size() > 0){
			img_vacant = false;
		}
		
		if(p_vacant == false){
			result = doc.select("p").eq(p_index).text();
		}else if(div_vacant == false){
			result = doc.select("div").eq(div_index).text();
		}else if(span_vacant == false){
			result = doc.select("span").eq(span_index).text();
		}else if(img_vacant == false){
			result = "본문에 이미지만 존재합니다.";
		}else{
			result = content;
			if(result.equals("<br>") || result.contains("&nbsp")){
				result = "";
			}
		}
		
		//한줄에 43줄!!
		if(result.length() > 86){
			result = result.substring(0, 80);
			result += ".....";
		}
		
		return result;
	}
	
	public static String removeTag(String html){
		html = html.replaceAll("&nbsp;", " ");
		return html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}
	
	public static String HttpCon(String address){
		try {
			URL url = new URL(address);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			StringBuffer sb = new StringBuffer();

			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}
