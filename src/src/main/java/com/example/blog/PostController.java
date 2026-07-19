package com.example.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "index";
    }

    @GetMapping("/post/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findById(id).orElse(null));
        return "view";
    }

    @GetMapping("/post/new")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "form";
    }

    @PostMapping("/post")
    public String save(Post post) {
        postService.save(post);
        return "redirect:/";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findById(id).orElse(null));
        return "form";
    }

    @PostMapping("/post/delete/{id}")
    public String delete(@PathVariable Long id) {
        postService.deleteById(id);
        return "redirect:/";
    }
}
