package com.edwbadillo.storedemo.product.category;

import com.edwbadillo.storedemo.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@EnableJpaAuditing
public class CategoryRepositoryTests {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private AuditingHandler auditingHandler;

    @Test
    void shouldPaginate() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Category category = new Category();
            category.setName("category" + i);
            categories.add(category);
        }

        categoryRepository.saveAll(categories);

        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Category> pageResult = categoryRepository.findAll(pageable);
        assertEquals(10, pageResult.getSize());
        assertEquals(15, pageResult.getTotalElements());
    }

    @Test
    void testExistsByNameIgnoreCase() {
        Category category = new Category();
        category.setName("category1");
        categoryRepository.save(category);

        assertTrue(categoryRepository.existsByNameIgnoreCase("category1"));
        assertFalse(categoryRepository.existsByNameIgnoreCase("category2"));
    }

    @Test
    void testExistsByNameIgnoreCaseAndIdNot() {
        Category category = new Category();
        category.setName("category1");
        categoryRepository.save(category);

        int excludeId = category.getId() + 1;
        assertTrue(categoryRepository.existsByNameIgnoreCaseAndIdNot("category1", excludeId));
        assertFalse(categoryRepository.existsByNameIgnoreCaseAndIdNot("category1", category.getId()));
        assertFalse(categoryRepository.existsByNameIgnoreCaseAndIdNot("category2", category.getId()));
        assertFalse(categoryRepository.existsByNameIgnoreCaseAndIdNot("category2", excludeId));
    }

    @Test
    void shouldFindById() {
        Category category = new Category();
        category.setName("category1");
        categoryRepository.save(category);
        Optional<Category> result = categoryRepository.findById(category.getId());

        assertTrue(result.isPresent());
        assertEquals(category.getId(), result.get().getId());
        assertEquals(category.getName(), result.get().getName());
    }

    @Test
    void shouldCategorySaved() {
        Category category = new Category();
        category.setName("My Category");
        category.setDescription("category1 description");
        category.setActive(true);
        categoryRepository.save(category);

        assertNotNull(category.getId());
        assertTrue(category.getId() > 0);
        assertEquals("My Category", category.getName());
        assertEquals("category1 description", category.getDescription());
        assertTrue(category.isActive());
    }

    @Test
    void shouldCategoryUpdated() {
        Category category = new Category();
        category.setName("Category1");
        categoryRepository.save(category);
        categoryRepository.flush();

        category.setName("Category updated");
        category.setDescription("category updated description");
        category.setActive(false);
        Category categoryUpdated = categoryRepository.save(category);
        categoryRepository.flush();

        assertEquals(category.getId(), categoryUpdated.getId());
        assertEquals("Category updated", categoryUpdated.getName());
        assertEquals("category updated description", categoryUpdated.getDescription());
        assertFalse(categoryUpdated.isActive());
    }

    @Test
    void shouldCategoryDeleted() {
        Category category = new Category();
        category.setName("category1");
        categoryRepository.save(category);
        categoryRepository.deleteById(category.getId());
        categoryRepository.flush();
        assertTrue(categoryRepository.findById(category.getId()).isEmpty());
    }

    @Test
    void shouldExistsById() {
        Category category = new Category();
        category.setName("category1");
        categoryRepository.save(category);
        categoryRepository.flush();
        assertTrue(categoryRepository.existsById(category.getId()));
        assertFalse(categoryRepository.existsById(category.getId() + 1));
    }

    @Test
    void shouldHasProducts() {
        Category category = new Category();
        category.setName("category1");
        categoryRepository.save(category);
        categoryRepository.flush();
        assertFalse(categoryRepository.hasProducts(category.getId()));

        Product product = new Product("Product1", "Description", category);
        em.persist(product);

        assertTrue(categoryRepository.hasProducts(category.getId()));
    }
}
