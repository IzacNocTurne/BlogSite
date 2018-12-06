package com.cos.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Character.UnicodeBlock;
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
	
	//크로스 사이트 스크립트 공격 방어
	public static String getReplace(String code) {	
		return code.replaceAll(" ", "&nbsp;").replaceAll("<", "&lt").replaceAll(">", "&gt").replaceAll("\n", "<br>");
	}
	
	//미리보기 화면을 위한 함수
	public static String preview(String content){
		
		Document doc = Jsoup.parse(content);
		Elements img_tag = doc.select("img");
		Elements iframe_tag = doc.select("iframe");
		
		String remove_content = removeTag(content);

		if(remove_content.length() == 0){
			if(img_tag.size() > 0 && iframe_tag.size() > 0){
				remove_content = "본문에 이미지와 영상만 존재합니다.";
			}else if((img_tag.size() > 0 && iframe_tag.size() == 0)){
				remove_content = "본문에 이미지만 존재합니다.";
			}else if((img_tag.size() == 0 && iframe_tag.size() > 0)){
				remove_content = "본문에 영상만 존재합니다.";
			}else{
				remove_content = "본문에 내용이 존재하지 않습니다.";
			}
		}else{
			//한줄에 영어만 하면 73줄, 한글로만 하면 50줄!!
			if(remove_content.length() > 50){
				remove_content = remove_content.substring(0, 50);
			}	
		}	
		
		return remove_content;
	}
	
	//모든 HTML 태그 제거
	public static String removeTag(String html){
		html = html.replaceAll("&nbsp;", " ");
		return html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	}
	
	// 자바 http 요청
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
	
	public static String makeYoutube(String content){
	    Document doc = Jsoup.parse(content);
	    Elements a_tag = doc.select("a");
	    
	    String iFrame="";
	    
	    System.out.println(a_tag.size());
	    for(int i=0; i< a_tag.size(); i++){
	    	if(a_tag.get(i).attr("href").contains("www.youtube.com/watch")){
		    	System.out.println("영상 링크가 존재합니다.");
		    	String href = a_tag.get(i).attr("href");
		    	String sp[] = href.split("=");
		    	String value = sp[1];
		    	iFrame = "<iframe id=\"player\" type=\"text/html\" width=\"726\" height=\"409\" src=\"http://www.youtube.com/embed/"+value+"\" frameborder=\"0\" webkitallowfullscreen=\"\" mozallowfullscreen=\"\" allowfullscreen=\"\"></iframe>";
		    	a_tag.get(i).after(iFrame);
		    }   
	    }
	    
	    
	    System.out.println(doc);
	    return doc.toString();
	}
	
	//자바 한글 검출기
    public static boolean containsHangul(String str)
    {
        for ( int i = 0 ; i < str.length( ) ; i++ )
        {
            char ch = str.charAt( i );
            Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of( ch );
            if ( UnicodeBlock.HANGUL_SYLLABLES.equals( unicodeBlock ) || 
                    UnicodeBlock.HANGUL_COMPATIBILITY_JAMO.equals( unicodeBlock )
                    || UnicodeBlock.HANGUL_JAMO.equals( unicodeBlock ) )
            {
                return true;
            }
        }
        return false;
    }
}
