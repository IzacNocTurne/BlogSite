package com.example.blog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BlogApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StorageService storageService;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testServeUploadWithKoreanAndSpaces() throws Exception {
		String originalFilename = "한글 테스트 123.png";
		byte[] content = "fake image content".getBytes();
		MockMultipartFile multipartFile = new MockMultipartFile(
				"file",
				originalFilename,
				"image/png",
				content
		);

		// Store file
		String storedFilename = storageService.store(multipartFile);
		assertThat(storedFilename).isNotNull();

		// Perform GET request for the uploaded file and verify response status and content-type
		mockMvc.perform(get("/uploads/" + storedFilename))
				.andExpect(status().isOk())
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().string("Content-Type", "image/png"));
	}

	@Test
	void testPostContentLimit() {
		// 1. Create a Category with a unique name
		Category category = new Category();
		category.setName("LimitTestCategory_" + System.currentTimeMillis() + "_" + System.nanoTime());
		category = categoryRepository.save(category);

		// 2. Save a post with exactly 2000 characters
		String content2000 = "a".repeat(2000);
		Post post = new Post();
		post.setTitle("Test Post with 2000 characters");
		post.setAuthor("TestAuthor");
		post.setCreatedAt(LocalDateTime.now());
		post.setCategory(category);
		post.setContent(content2000);

		Post savedPost = postRepository.saveAndFlush(post);
		assertThat(savedPost.getId()).isNotNull();
		assertThat(savedPost.getContent()).hasSize(2000);

		// Clean up the saved post
		postRepository.delete(savedPost);
		postRepository.flush();
	}

	@Test
	void testPostContentExceedsLimit() {
		// Create a Category with a unique name
		Category category = new Category();
		category.setName("LimitExceedCategory_" + System.currentTimeMillis() + "_" + System.nanoTime());
		category = categoryRepository.save(category);

		// 3. Save a post with 2001 characters - should fail due to database column limit
		String content2001 = "a".repeat(2001);
		Post post2001 = new Post();
		post2001.setTitle("Test Post with 2001 characters");
		post2001.setAuthor("TestAuthor2");
		post2001.setCreatedAt(LocalDateTime.now());
		post2001.setCategory(category);
		post2001.setContent(content2001);

		// In Spring Boot JPA, flush causes Hibernate to write to DB and throw exception if constraints fail
		assertThrows(Exception.class, () -> {
			postRepository.saveAndFlush(post2001);
		});
	}
}
