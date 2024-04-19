package com.edwbadillo.storedemo.product;

import com.edwbadillo.storedemo.product.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProductRepository productRepository;

    private Category category;
    private Product product;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setName("category1");
        em.persist(category);

        product = new Product("Product1", "Description", category);
        product.setPrice(19.9);
        product.setQuantity(5);
    }

    @Test
    void shouldPaginate() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Product product = new Product("Product" + i, "Description", category);
            product.setPrice(19.9);
            product.setQuantity(5);
            products.add(product);
        }

        productRepository.saveAll(products);

        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<Product> pageResult = productRepository.findAll(pageable);
        assertEquals(10, pageResult.getSize());
        assertEquals(15, pageResult.getTotalElements());
    }

    @Test
    void shouldProductSavedThenFoundById() {
        productRepository.save(product);
        em.detach(product);

        Optional<Product> result = productRepository.findById(product.getId());
        assertTrue(result.isPresent());

        assertEquals(result.get().getId(), product.getId());
        assertEquals(result.get().getName(), product.getName());
        assertEquals(result.get().getCategory(), product.getCategory());
        assertEquals(result.get().getPrice(), product.getPrice());
        assertEquals(result.get().getQuantity(), product.getQuantity());
    }

    @Test
    void shouldProductUpdated() {
        Product savedProduct = productRepository.save(product);
        productRepository.flush();

        savedProduct.setName("Product updated");
        savedProduct.setDescription("Description updated");
        savedProduct.setPrice(29.9);
        savedProduct.setQuantity(10);
        Product updatedProduct = productRepository.save(savedProduct);
        productRepository.flush();

        Optional<Product> result = productRepository.findById(product.getId());
        assertTrue(result.isPresent());
        assertEquals("Product updated", updatedProduct.getName());
        assertEquals("Description updated", updatedProduct.getDescription());
        assertEquals(29.9, updatedProduct.getPrice());
        assertEquals(10, updatedProduct.getQuantity());
    }

    @Test
    void shouldProductDeleted() {
        Product savedProduct = productRepository.save(product);
        productRepository.deleteById(savedProduct.getId());
        assertFalse(productRepository.existsById(savedProduct.getId()));
    }

    @Test
    void shouldExistsByNameIgnoreCase() {
        productRepository.save(product);

        assertTrue(productRepository.existsByNameIgnoreCase("product1"));
        assertFalse(productRepository.existsByNameIgnoreCase("product2"));
    }

    @Test
    void shouldExistsByNameIgnoreCaseAndIdNot() {
        productRepository.save(product);

        int excludeId = product.getId() + 1;
        assertTrue(productRepository.existsByNameIgnoreCaseAndIdNot("product1", excludeId));
        assertFalse(productRepository.existsByNameIgnoreCaseAndIdNot("product1", product.getId()));
        assertFalse(productRepository.existsByNameIgnoreCaseAndIdNot("product2", product.getId()));
        assertFalse(productRepository.existsByNameIgnoreCaseAndIdNot("product2", excludeId));
    }
}
