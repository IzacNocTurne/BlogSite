package com.example.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            // Category category1 = new Category();
            // category1.setName("Java");
            // categoryRepository.save(category1);

            // Category category2 = new Category();
            // category2.setName("Spring");
            // categoryRepository.save(category2);

            // Category category3 = new Category();
            // category3.setName("Database");
            // categoryRepository.save(category3);

            // Category category4 = new Category();
            // category4.setName("재건축");
            // categoryRepository.save(category4);
        }
    }
}
