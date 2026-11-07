package com.example.blog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    // google.com, www.google.co.kr 등 국가별 구글 도메인
    private static final Pattern GOOGLE_HOST = Pattern.compile("(?:.+\\.)?google\\.[a-z]{2,3}(?:\\.[a-z]{2})?");

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReferralLogRepository referralLogRepository;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(Integer id) {
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        if (post.getCreatedAt() == null) {
            post.setCreatedAt(LocalDateTime.now());
        }
        return postRepository.save(post);
    }

    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void viewPost(Post post, String referer) {
        logger.info("Viewing post ID: {}, Current ViewCount: {}, Referer: {}", post.getId(), post.getViewCount(), referer);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);

        if (referer != null && !referer.isEmpty()) {
            String searchEngine = extractSearchEngine(referer);
            if (searchEngine != null) {
                ReferralLog log = new ReferralLog();
                log.setPost(post);
                log.setSearchEngine(searchEngine);
                referralLogRepository.save(log);
            }
        }
    }

    private String extractSearchEngine(String referer) {
        String host;
        try {
            host = new URI(referer).getHost();
        } catch (URISyntaxException e) {
            return null;
        }
        if (host == null) {
            return null;
        }

        // 주소 전체가 아닌 호스트만 검사해 쿼리 문자열에 포함된 도메인을 오인하지 않음
        String lowerHost = host.toLowerCase();
        if (GOOGLE_HOST.matcher(lowerHost).matches()) {
            return "Google";
        } else if (isDomainOrSubdomain(lowerHost, "naver.com")) {
            return "Naver";
        } else if (isDomainOrSubdomain(lowerHost, "daum.net")) {
            return "Daum";
        }
        // Add more search engines as needed
        return null;
    }

    private boolean isDomainOrSubdomain(String host, String domain) {
        return host.equals(domain) || host.endsWith("." + domain);
    }

    public List<Post> getTopViewedPosts() {
        return postRepository.findTop5ByOrderByViewCountDescIdDesc();
    }

    public List<Post> getTop5ReferredPosts() {
        return referralLogRepository.findTopPostsByReferralCount(PageRequest.of(0, 5));
    }

    public List<ReferralStats> getReferralStats() {
        return referralLogRepository.findReferralStats();
    }

    @Transactional
    public void clearStats() {
        logger.info("Clearing all referral logs and resetting view counts.");
        referralLogRepository.deleteAll(); // Delete all referral logs

        List<Post> allPosts = postRepository.findAll();
        for (Post post : allPosts) {
            post.setViewCount(0);
            postRepository.save(post);
        }
    }
}
