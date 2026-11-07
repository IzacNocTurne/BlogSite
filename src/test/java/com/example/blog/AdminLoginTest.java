package com.example.blog;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "blog.admin.password=configured-test-password",
        "spring.datasource.url=jdbc:h2:mem:adminlogin;DB_CLOSE_DELAY=-1"
})
@AutoConfigureMockMvc
class AdminLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void configuredPasswordLogsIn() throws Exception {
        mockMvc.perform(formLogin().user("admin").password("configured-test-password"))
                .andExpect(authenticated().withRoles("ADMIN"));
    }

    @Test
    void oldHardcodedPasswordIsRejected() throws Exception {
        mockMvc.perform(formLogin().user("admin").password("password"))
                .andExpect(unauthenticated());
    }
}
