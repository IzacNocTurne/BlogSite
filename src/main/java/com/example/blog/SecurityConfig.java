package com.example.blog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                // 시딩용 URL 명시적 허용
                .antMatchers("/seed-data").permitAll()
                // H2 콘솔은 ADMIN 권한만 허용
                .antMatchers("/h2-console/**").hasRole("ADMIN")
                // /admin 으로 시작하는 모든 경로는 ADMIN 권한 필요
                .antMatchers("/admin/**").hasRole("ADMIN")
                // 카테고리 관리 기능은 ADMIN 권한 필요
                .antMatchers("/category/manage", "/category/new", "/category/save", "/category/edit/**", "/category/delete/**").hasRole("ADMIN")
                // 글쓰기, 수정, 삭제 기능은 ADMIN 권한 필요
                .antMatchers("/post/new", "/post/edit/**", "/post/delete/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/post").hasRole("ADMIN")
                // 그 외 모든 요청은 허용
                .anyRequest().permitAll()
            .and()
            .formLogin() // 기본 로그인 폼 활성화
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/") // 로그아웃 성공 시 홈으로
                .permitAll()
            .and()
            .csrf().disable() // 개발 편의를 위해 CSRF 일단 비활성화 (필요시 활성화)
            .headers().frameOptions().sameOrigin();
            
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 메모리에 고정된 관리자 계정 생성
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("password")) // 패스워드: password
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
