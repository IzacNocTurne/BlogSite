package com.example.blog;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByCategory(Category category);
    Page<Post> findByCategory(Category category, Pageable pageable);
    List<Post> findByTitle(String title);
}
