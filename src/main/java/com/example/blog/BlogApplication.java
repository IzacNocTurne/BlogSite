package com.example.blog;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		// 서버 시간대(UTC)와 관계없이 한국 시간으로 처리. DB 연결이 만들어지기 전에 설정해야 함
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		SpringApplication.run(BlogApplication.class, args);
	}

}
