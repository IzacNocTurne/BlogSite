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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {

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

    @GetMapping("/category/{id}")
    public String viewByCategory(@PathVariable Integer id, @RequestParam(defaultValue = "0") int page, Model model) {
        Category category = categoryRepository.findById(id).orElse(null);
        // 정렬 순서 안정화 (createdAt, id 둘 다 DESC)
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")));
        
        if (category != null) {
            model.addAttribute("postsPage", postRepository.findByCategory(category, pageRequest)); 
            model.addAttribute("selectedCategory", category);
        } else {
            model.addAttribute("postsPage", postRepository.findAll(pageRequest));
        }
        
        // 해당 카테고리의 최신글 한 건 혹은 전체 최신글
        List<Post> latestPosts;
        if (category != null) {
            latestPosts = postRepository.findByCategory(category, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")))).getContent();
        } else {
            latestPosts = postRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")))).getContent();
        }
        
        if (!latestPosts.isEmpty()) {
            model.addAttribute("post", latestPosts.get(0));
        }

        return "index";
    }

    @GetMapping("/")
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")));
        Page<Post> postsPage = postRepository.findAll(pageRequest);
        model.addAttribute("postsPage", postsPage);
        
        // 메인 페이지에서는 가장 최신 게시물을 기본으로 보여줌
        List<Post> latestPosts = postRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")))).getContent();
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
        // 상단 리스트를 위한 페이징 처리 및 정렬 고정
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id")));
        
        Category category = (categoryId != null) ? categoryRepository.findById(categoryId).orElse(null) : null;
        if (category != null) {
            model.addAttribute("postsPage", postRepository.findByCategory(category, pageRequest));
            model.addAttribute("selectedCategory", category);
        } else {
            model.addAttribute("postsPage", postRepository.findAll(pageRequest));
        }

        return postService.findById(id).map(post -> {
            postService.viewPost(post, referer);
            model.addAttribute("post", post); // 선택된 게시글
            return "index"; // view 대신 index 사용
        }).orElse("redirect:/");
    }

    @GetMapping("/prologue")
    public String prologue(Model model) {
        model.addAttribute("posts", postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by(Sort.Direction.DESC, "id"))));
        return "prologue";
    }

    @GetMapping("/post/new")
    public String newPost(Model model) {
        Post post = new Post();
        post.setAuthor("Gemini"); // 기본 작성자 설정
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
            post.setAuthor("Gemini");
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
}
