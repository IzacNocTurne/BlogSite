package com.example.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:adminstats;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
class AdminStatsTest {

    private static final String NO_REFERRALS = "아직 검색 유입 기록이 없습니다";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReferralLogRepository referralLogRepository;

    @AfterEach
    void cleanUp() {
        referralLogRepository.deleteAll();
        postRepository.deleteAll();
    }

    private Post createPost(String title) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent("본문");
        post.setAuthor("테스트");
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    private void visit(Post post, String referer) throws Exception {
        mockMvc.perform(get("/post/" + post.getId()).header("Referer", referer))
                .andExpect(status().isOk());
    }

    private List<ReferralStats> referralStats() {
        return referralLogRepository.findReferralStats();
    }

    @Test
    void emptyStatsExplainWhyNothingIsShown() throws Exception {
        mockMvc.perform(get("/admin/stats"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(NO_REFERRALS)))
                .andExpect(content().string(not(containsString("id=\"referralChart\""))));
    }

    @Test
    void viewsAppearWithoutSearchReferrals() throws Exception {
        Post post = createPost("직접 방문 글");
        mockMvc.perform(get("/post/" + post.getId())).andExpect(status().isOk());

        mockMvc.perform(get("/admin/stats"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(NO_REFERRALS)))
                .andExpect(content().string(containsString("직접 방문 글")));
    }

    @Test
    void searchReferralsAppearInChartAndTopPosts() throws Exception {
        Post post = createPost("검색 유입 글");
        visit(post, "https://www.google.com/");
        visit(post, "https://search.naver.com/search.naver?query=x");

        mockMvc.perform(get("/admin/stats"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("id=\"referralChart\"")))
                .andExpect(content().string(not(containsString(NO_REFERRALS))))
                .andExpect(content().string(containsString("검색 유입 글")));
    }

    @Test
    void countryGoogleDomainsAreCounted() throws Exception {
        visit(createPost("한국 구글 글"), "https://www.google.co.kr/");

        assertThat(referralStats()).hasSize(1);
        assertThat(referralStats().get(0).getSearchEngine()).isEqualTo("Google");
    }

    @Test
    void domainsInsideOtherUrlsAreNotCounted() throws Exception {
        Post post = createPost("오인 방지 글");
        visit(post, "https://example.org/?from=google.com");
        visit(post, "https://notgoogle.com/");
        visit(post, "https://google.com.example.org/");
        visit(post, "not a url");

        assertThat(referralStats()).isEmpty();
    }
}
