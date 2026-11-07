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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.datasource.url=jdbc:h2:mem:siteseo;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
class SiteSeoTest {

    private static final String SITE_NAME = "재건축 정보 분석 블로그";
    private static final int POST_COUNT = 6; // 한 페이지(5개)를 넘겨 2페이지가 생기도록 함

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Post latestPost;

    @BeforeEach
    void createPosts() {
        category = new Category();
        category.setName("SeoTestCategory");
        category = categoryRepository.save(category);

        for (int i = 0; i < POST_COUNT; i++) {
            Post post = new Post();
            post.setTitle("테스트 글 " + i);
            post.setContent("테스트 본문 " + i);
            post.setAuthor("테스트");
            post.setCategory(category);
            post.setCreatedAt(LocalDateTime.now());
            latestPost = postRepository.save(post);
        }
    }

    @AfterEach
    void deletePosts() {
        postRepository.deleteAll();
        categoryRepository.delete(category);
    }

    @Test
    void robotsPointsToSitemapOnRequestedHost() throws Exception {
        mockMvc.perform(get("http://blog.example.org/robots.txt"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sitemap: http://blog.example.org/sitemap.xml")))
                .andExpect(content().string(not(containsString("localhost"))));
    }

    @Test
    void missingContentReturnsNotFound() throws Exception {
        for (String path : List.of(
                "/post/999999",
                "/category/999999",
                "/category/" + 999999 + "?page=1",
                "/?page=-1",
                "/?page=999",
                "/category/%d?page=-1",
                "/category/%d?page=999")) {
            mockMvc.perform(get(String.format(path, category.getId())))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void existingContentStaysReachable() throws Exception {
        for (String path : List.of(
                "/",
                "/?page=1",
                "/category/" + category.getId(),
                "/category/" + category.getId() + "?page=1",
                "/post/" + latestPost.getId(),
                "/post/" + latestPost.getId() + "?page=-1",
                "/post/" + latestPost.getId() + "?page=999",
                "/robots.txt",
                "/sitemap.xml",
                "/rss",
                "/favicon.ico",
                "/css/default-og-image.png")) {
            mockMvc.perform(get(path)).andExpect(status().isOk());
        }
    }

    @Test
    void browsersGetHtmlNotFoundPage() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.TEXT_HTML));

        ResponseEntity<String> response = restTemplate.exchange(
                "/post/999999", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .contains("페이지를 찾을 수 없습니다")
                .contains(SITE_NAME)
                .contains("noindex");
    }

    @Test
    void shareImageUsesAbsoluteUrl() throws Exception {
        mockMvc.perform(get("http://blog.example.org/"))
                .andExpect(content().string(containsString(
                        "property=\"og:image\" content=\"http://blog.example.org/css/default-og-image.png\"")));
    }

    @Test
    void pagerDoesNotLinkOutsideExistingPages() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(content().string(not(containsString("page=-1"))))
                .andExpect(content().string(containsString("/?page=1")));
        mockMvc.perform(get("/?page=1"))
                .andExpect(content().string(not(containsString("page=2"))))
                .andExpect(content().string(containsString("/?page=0")));
    }

    @Test
    void pagesUseOneSiteName() throws Exception {
        for (String path : List.of("/", "/about", "/contact", "/privacy", "/prologue", "/rss")) {
            mockMvc.perform(get(path))
                    .andExpect(content().string(containsString(SITE_NAME)))
                    .andExpect(content().string(not(containsString("마이 블로그"))));
        }
    }

    @Test
    void prologueHasNoImagePlaceholder() throws Exception {
        mockMvc.perform(get("/prologue"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("이미지 없음"))));
    }

    @Test
    void privacyPolicyDoesNotMentionMissingContactForm() throws Exception {
        mockMvc.perform(get("/privacy"))
                .andExpect(content().string(not(containsString("폼"))));
    }

    @Test
    void prologueSendsOnlyAnExcerptOfLongPosts() throws Exception {
        latestPost.setContent("가".repeat(5000));
        postRepository.save(latestPost);

        mockMvc.perform(get("/prologue"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("가".repeat(100))))
                .andExpect(content().string(not(containsString("가".repeat(201)))));
    }

    @Test
    void contactPageShowsRealEmail() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(content().string(containsString("mailto:sonth2000@gmail.com")))
                .andExpect(content().string(not(containsString("example.com"))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void newPostsDefaultToAdminAuthor() throws Exception {
        mockMvc.perform(get("/post/new"))
                .andExpect(content().string(containsString("관리자")))
                .andExpect(content().string(not(containsString("Gemini"))));
    }

    @Test
    void seedEndpointNoLongerExists() throws Exception {
        long postsBefore = postRepository.count();

        mockMvc.perform(get("/seed-data")).andExpect(status().isNotFound());

        assertThat(postRepository.count()).isEqualTo(postsBefore);
    }
}
