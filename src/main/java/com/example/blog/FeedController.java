package com.example.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class FeedController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String getSitemap(HttpServletRequest request) {
        String baseUrl = getBaseUrl(request);
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        // 메인 페이지 및 필수 페이지
        addUrl(sb, baseUrl + "/", "1.0", null);
        addUrl(sb, baseUrl + "/about", "0.8", null);
        addUrl(sb, baseUrl + "/contact", "0.8", null);
        addUrl(sb, baseUrl + "/privacy", "0.5", null);

        // 모든 게시글 (최신순)
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            String lastMod = (post.getCreatedAt() != null) ? post.getCreatedAt().format(formatter) : null;
            addUrl(sb, baseUrl + "/post/" + post.getId(), "0.9", lastMod);
        }

        sb.append("</urlset>");
        return sb.toString();
    }

    @GetMapping(value = "/rss", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String getRss(HttpServletRequest request) {
        String baseUrl = getBaseUrl(request);
        List<Post> posts = postRepository.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        sb.append("<rss version=\"2.0\">");
        sb.append("<channel>");
        sb.append("<title>마이 블로그</title>");
        sb.append("<link>").append(baseUrl).append("</link>");
        sb.append("<description>나의 일상과 정보를 공유하는 블로그</description>");

        for (Post post : posts) {
            sb.append("<item>");
            sb.append("<title>").append(escapeXml(post.getTitle())).append("</title>");
            sb.append("<link>").append(baseUrl).append("/post/").append(post.getId()).append("</link>");
            sb.append("<description>").append(escapeXml(post.getContent())).append("</description>");
            sb.append("<guid>").append(baseUrl).append("/post/").append(post.getId()).append("</guid>");
            sb.append("</item>");
        }

        sb.append("</channel>");
        sb.append("</rss>");
        return sb.toString();
    }

    private void addUrl(StringBuilder sb, String loc, String priority, String lastmod) {
        sb.append("<url>");
        sb.append("<loc>").append(loc).append("</loc>");
        if (lastmod != null) {
            sb.append("<lastmod>").append(lastmod).append("</lastmod>");
        }
        sb.append("<priority>").append(priority).append("</priority>");
        sb.append("</url>");
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath);
        return url.toString();
    }

    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
