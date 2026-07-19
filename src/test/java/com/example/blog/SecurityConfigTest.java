package com.example.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void h2ConsoleShouldBeProtected() throws Exception {
        // 비인증 상태에서 /h2-console 접근 시 로그인 페이지(302 Redirect) 또는 Access Denied가 발생해야 함
        mockMvc.perform(get("/h2-console"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void publicPagesShouldBeAccessible() throws Exception {
        // 일반 사용자는 메인 페이지(/)에 로그인 없이 접근 가능해야 함
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void categoryManagementShouldBeProtected() throws Exception {
        // 비인증 상태에서 카테고리 관리(/category/manage) 접근 시 302 Redirect 발생
        mockMvc.perform(get("/category/manage"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void postCreationShouldBeProtected() throws Exception {
        // 비인증 상태에서 새 글 작성(/post/new) 접근 시 302 Redirect 발생
        mockMvc.perform(get("/post/new"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void categoryManagementAccessibleByAdmin() throws Exception {
        // ADMIN 권한을 가진 사용자는 카테고리 관리 접근 가능해야 함
        mockMvc.perform(get("/category/manage"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void postCreationAccessibleByAdmin() throws Exception {
        // ADMIN 권한을 가진 사용자는 글쓰기 페이지 접근 가능해야 함
        mockMvc.perform(get("/post/new"))
                .andExpect(status().isOk());
    }
}
