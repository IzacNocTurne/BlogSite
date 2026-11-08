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

    private static final String SITE_NAME = "부동산 정보 분석 블로그";
    private static final int POST_COUNT = 11; // 한 페이지(10개)를 넘겨 2페이지가 생기도록 함

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
    void everyPublicPageSharesOneMenu() throws Exception {
        for (String path : List.of("/", "/prologue", "/about", "/contact", "/privacy",
                "/category/" + category.getId(), "/post/" + latestPost.getId())) {
            mockMvc.perform(get(path))
                    .andExpect(content().string(containsString("data-bs-target=\"#navbarNav\"")))
                    .andExpect(content().string(containsString("bootstrap.bundle.min.js")))
                    .andExpect(content().string(containsString("href=\"/prologue\">전체 글</a>")))
                    .andExpect(content().string(containsString("href=\"/about\">소개</a>")))
                    .andExpect(content().string(containsString("href=\"/contact\">연락처</a>")))
                    .andExpect(content().string(containsString("href=\"/privacy\">개인정보처리방침</a>")))
                    .andExpect(content().string(not(containsString("프롤로그"))))
                    .andExpect(content().string(not(containsString("navbar-dark"))))
                    .andExpect(content().string(not(containsString("bootstrap@5.3.0"))));
        }
    }

    @Test
    void homeListsExcerptsInsteadOfFullLatestPost() throws Exception {
        latestPost.setContent("## 요약 제목\n\n" + "나".repeat(3000));
        postRepository.save(latestPost);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("/post/" + latestPost.getId())))
                .andExpect(content().string(containsString("나".repeat(100))))
                .andExpect(content().string(not(containsString("나".repeat(161)))))
                .andExpect(content().string(not(containsString("## 요약"))));
    }

    @Test
    void postPageRendersMarkdownSafely() throws Exception {
        latestPost.setContent(String.join("\n",
                "■ 옛 방식 소제목",
                "",
                "## 사업 개요",
                "",
                "**시공사**는 미정입니다.",
                "",
                "| 단지 | 단계 |",
                "|---|---|",
                "| 1단지 | 조합설립 |",
                "",
                "출처: https://news.example.com/a",
                "",
                "<script>alert(1)</script>",
                "[나쁜 링크](javascript:alert(1))",
                "",
                "첫째 줄",
                "둘째 줄"));
        postRepository.save(latestPost);

        String html = mockMvc.perform(get("/post/" + latestPost.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(html)
                .contains("<h2>옛 방식 소제목</h2>")
                .contains("<h2>사업 개요</h2>")
                .contains("<strong>시공사</strong>")
                .contains("<td>1단지</td>")
                .contains("href=\"https://news.example.com/a\"")
                .contains("rel=\"nofollow noopener noreferrer\"")
                .contains("첫째 줄<br>")
                .contains("&lt;script&gt;")
                .doesNotContain("<script>alert")
                .doesNotContain("javascript:alert");
    }

    @Test
    void postPageShowsBodyBeforeOtherPosts() throws Exception {
        latestPost.setContent("본문표시확인");
        postRepository.save(latestPost);

        String html = mockMvc.perform(get("/post/" + latestPost.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(html).contains("SeoTestCategory의 다른 글");
        assertThat(html.indexOf("class=\"post-content\"")).isPositive().isLessThan(html.indexOf("SeoTestCategory의 다른 글"));
        assertThat(html).contains("<meta name=\"description\" content=\"본문표시확인\">");
    }

    @Test
    void feedUsesPlainTextDescriptions() throws Exception {
        latestPost.setContent("## 제목\n\n**굵은** 본문");
        postRepository.save(latestPost);

        mockMvc.perform(get("/rss"))
                .andExpect(content().string(containsString("<description>제목 굵은 본문</description>")));
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
