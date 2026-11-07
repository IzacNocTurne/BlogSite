package com.example.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {

    private static final String DEFAULT_AUTHOR = "관리자";
    private static final int PAGE_SIZE = 5;
    // 정렬 순서 안정화 (createdAt, id 둘 다 DESC)
    private static final Sort LIST_SORT = Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"));

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StorageService storageService;

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    // 공유 이미지처럼 절대 주소가 필요한 곳에서 사용
    @ModelAttribute("baseUrl")
    public String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    }

    @GetMapping("/category/{id}")
    public String viewByCategory(@PathVariable Integer id, @RequestParam(defaultValue = "0") int page, Model model) {
        Category category = categoryRepository.findById(id).orElseThrow(PostController::notFound);
        Page<Post> postsPage = postRepository.findByCategory(category, listPage(page));
        requireExistingPage(postsPage);
        model.addAttribute("postsPage", postsPage);
        model.addAttribute("selectedCategory", category);

        // 해당 카테고리의 최신글 한 건
        List<Post> latestPosts = postRepository.findByCategory(category, PageRequest.of(0, 1, LIST_SORT)).getContent();
        if (!latestPosts.isEmpty()) {
            model.addAttribute("post", latestPosts.get(0));
        }

        return "index";
    }

    @GetMapping("/")
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Post> postsPage = postRepository.findAll(listPage(page));
        requireExistingPage(postsPage);
        model.addAttribute("postsPage", postsPage);

        // 메인 페이지에서는 가장 최신 게시물을 기본으로 보여줌
        List<Post> latestPosts = postRepository.findAll(PageRequest.of(0, 1, LIST_SORT)).getContent();
        if (!latestPosts.isEmpty()) {
            model.addAttribute("post", latestPosts.get(0));
        }
        
        return "index";
    }

    @GetMapping("/post/{id:[0-9]+}")
    public String view(@PathVariable Integer id, 
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) Integer categoryId,
                       @RequestHeader(value = "Referer", required = false) String referer, 
                       Model model) {
        // 상단 리스트를 위한 페이징 처리 (page 값이 잘못돼도 글 자체는 보여줌)
        PageRequest pageRequest = listPage(Math.max(page, 0));

        Category category = (categoryId != null) ? categoryRepository.findById(categoryId).orElse(null) : null;
        if (category != null) {
            model.addAttribute("postsPage", postRepository.findByCategory(category, pageRequest));
            model.addAttribute("selectedCategory", category);
        } else {
            model.addAttribute("postsPage", postRepository.findAll(pageRequest));
        }

        Post post = postService.findById(id).orElseThrow(PostController::notFound);
        postService.viewPost(post, referer);
        model.addAttribute("post", post); // 선택된 게시글
        return "index"; // view 대신 index 사용
    }

    @GetMapping("/prologue")
    public String prologue(Model model) {
        model.addAttribute("posts", postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"))));
        return "prologue";
    }

    @GetMapping("/post/new")
    public String newPost(Model model) {
        Post post = new Post();
        post.setAuthor(DEFAULT_AUTHOR); // 기본 작성자 설정
        model.addAttribute("post", post);
        return "form";
    }

    @PostMapping("/post")
    public String save(Post post, @RequestParam("file") MultipartFile file) {
        String imagePath = storageService.store(file);
        if (imagePath != null) {
            post.setImagePath(imagePath);
        }
        if (post.getId() == null) { // 새 게시물일 경우
            post.setAuthor(DEFAULT_AUTHOR);
            post.setCreatedAt(LocalDateTime.now());
        }
        postService.save(post);
        return "redirect:/post/" + post.getId();
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("post", postService.findById(id).orElse(null));
        return "form";
    }

    @PostMapping("/post/delete/{id}")
    public String delete(@PathVariable Integer id) {
        postService.deleteById(id);
        return "redirect:/";
    }

    private static PageRequest listPage(int page) {
        if (page < 0) {
            throw notFound();
        }
        return PageRequest.of(page, PAGE_SIZE, LIST_SORT);
    }

    private static void requireExistingPage(Page<Post> postsPage) {
        if (postsPage.getNumber() > 0 && postsPage.isEmpty()) {
            throw notFound();
        }
    }

    private static ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
