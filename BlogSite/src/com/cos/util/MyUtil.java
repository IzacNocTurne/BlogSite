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
		
		//p태그
		String p_value = "";
		int p_length = 0;
		int p_index = 0;
		boolean p_vacant = true;
		
		//img태그
		boolean img_vacant = true;
		
		//공백체크(아무글도 적지 않으면 <br>만 생겨 있다.)
		boolean br_vacant = true;
		
		//태그 없이 글만 있는 것 체크
		boolean notag_vacant = true;
		
		Document doc = Jsoup.parse(content);
		Elements img_tag = doc.select("img");
		Elements p_tag = doc.select("p");	
		Elements br_tag = doc.select("br");
		//System.out.println(img_tag);
		//System.out.println(p_tag);
		//System.out.println(br_tag);
		
/*		for(Element e : p_tag){
			p_count++;
		}*/
		
		for(int i=0; i<p_tag.size(); i++){
			p_value = doc.select("p").eq(i).text();
			if(p_length < p_value.length()){
				p_length = p_value.length();
				if(!p_value.equals("")){
					p_index = i;
					p_vacant = false;
				}
			}
		}
		
		if(img_tag.size() > 0){
			img_vacant = false;
		}
		
		if(br_tag.size() > 0){
			br_vacant = false;
		}
		
		if(content.length() > 0){
			notag_vacant = false;
		}
		
		if(p_vacant == false){
			result = doc.select("p").eq(p_index).text();
		}else if(img_vacant == false){
			result = "본문에 이미지만 존재합니다.";
		}else if(br_vacant == false){
			result = "본문에 내용이 존재하지 않습니다.";
		}else if(notag_vacant == false){
			result = content;
		}else{
			result = "본문에 내용이 존재하지 않습니다.";
		}
		
		System.out.println(naming+result);
		
		//한줄에 43줄!!
		if(result.length() > 43){
			result = result.substring(0, 38);
			result += "...";
		}
		
		return result;
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
