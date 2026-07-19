package com.example.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/manage")
    public String list(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "category/list";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        return "category/form";
    }

    @PostMapping("/save")
    public String save(Category category) {
        categoryRepository.save(category);
        return "redirect:/category/manage";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("category", categoryRepository.findById(id).orElse(null));
        return "category/form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            postRepository.findByCategory(category).forEach(post -> {
                post.setCategory(null);
                postRepository.save(post);
            });
            categoryRepository.deleteById(id);
        }
        return "redirect:/category/manage";
    }
}
