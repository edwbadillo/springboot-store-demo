package com.edwbadillo.storedemo.product;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.exception.InvalidDataException;
import com.edwbadillo.storedemo.product.category.Category;
import com.edwbadillo.storedemo.product.category.CategoryRepository;
import com.edwbadillo.storedemo.product.category.dto.CategoryInfo;
import com.edwbadillo.storedemo.product.dto.ProductDetails;
import com.edwbadillo.storedemo.product.dto.ProductInfo;
import com.edwbadillo.storedemo.product.dto.ProductMapper;
import com.edwbadillo.storedemo.product.dto.ProductRegister;
import com.edwbadillo.storedemo.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService service;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductMapper productMapper;

    private Product product;
    private Category category;


    @BeforeEach
    void setUp() {
        category = new Category(1, "Category 1", "Category description", true);
        product = new Product(1, "Product 1", "Product description", true, 19.9, 10, category);
    }

    @Test
    void shouldPaginateProducts() {
        Page<Product> page = new PageImpl<>(List.of(product));
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        when(productRepository.findAll(pageable)).thenReturn(page);
        when(productMapper.getPage(page)).thenReturn(new PageDTO<>(
                List.of(new ProductInfo(
                        product.getId(),
                        product.getName(),
                        product.isActive(),
                        product.getPrice(),
                        product.getQuantity()
                )),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getNumberOfElements()
        ));

        PageDTO<ProductInfo> result = service.paginate(pageable);

        assertEquals(page.getTotalElements(), result.totalCount());
        assertEquals(page.getTotalPages(), result.totalPages());
        assertEquals(page.getNumber(), result.numPage());
        assertEquals(page.getNumberOfElements(), result.numItems());
    }

    @Test
    void shouldGetProductById() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productMapper.getDetails(product)).thenReturn(buildProductDetails());

        ProductDetails result = service.getById(product.getId());

        assertEquals(product.getId(), result.id());
        assertEquals(product.getName(), result.name());
        assertEquals(product.getDescription(), result.description());
        assertEquals(product.isActive(), result.isActive());
        assertEquals(product.getPrice(), result.price());
        assertEquals(product.getQuantity(), result.quantity());

        assertEquals(category.getId(), result.category().id());
        assertEquals(category.getName(), result.category().name());
        assertEquals(category.isActive(), result.category().isActive());
    }

    @Test
    void shouldThrowExceptionWhenGetProductByIdNotFound() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class, () -> service.getById(product.getId())
        );

        assertEquals("Product " + product.getId() + " not found", exception.getMessage());
    }

    @Test
    void shouldCreateProduct() {
        ProductRegister data = buildProductRegisterData();

        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.of(category));
        when(productRepository.existsByNameIgnoreCase(data.name())).thenReturn(false);
        when(productMapper.getEntity(data)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.getDetails(product)).thenReturn(buildProductDetails());

        ProductDetails result = service.create(data);

        assertEquals(product.getId(), result.id());
        assertEquals(product.getName(), result.name());
        assertEquals(product.getDescription(), result.description());
        assertEquals(product.isActive(), result.isActive());
        assertEquals(product.getPrice(), result.price());
        assertEquals(product.getQuantity(), result.quantity());
    }

    @Test
    void shouldThrowExceptionWhenCreateWithAlreadyExistsByName() {
        ProductRegister data = buildProductRegisterData();

        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.of(category));
        when(productRepository.existsByNameIgnoreCase(data.name())).thenReturn(true);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.create(data)
        );

        assertEquals("already_exists", exception.getType());
        assertEquals("name", exception.getField());
        assertEquals("Name already exists", exception.getMessage());
        assertEquals(data.name(), exception.getValue());
    }

    @Test
    void shouldThrowExceptionWhenCreateCategoryNotFound() {
        ProductRegister data = buildProductRegisterData();
        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.empty());

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.create(data)
        );

        assertEquals("invalid_value", exception.getType());
        assertEquals("categoryId", exception.getField());
        assertEquals("Category not found", exception.getMessage());
        assertEquals(data.categoryId(), exception.getValue());
    }

    @Test
    void shouldThrowExceptionWhenCreateWithCategoryNotActive() {
        ProductRegister data = buildProductRegisterData();
        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.of(category));
        category.setActive(false);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.create(data)
        );

        assertEquals("invalid_value", exception.getType());
        assertEquals("categoryId", exception.getField());
        assertEquals("Category not active", exception.getMessage());
        assertEquals(data.categoryId(), exception.getValue());
    }

    @Test
    void shouldUpdateProduct() {
        ProductRegister data = buildProductRegisterData();

        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.of(category));
        when(productRepository.existsByNameIgnoreCase(data.name())).thenReturn(false);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productMapper.getEntity(data)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.getDetails(product)).thenReturn(buildProductDetails());

        ProductDetails result = service.update(product.getId(), data);

        assertEquals(product.getId(), result.id());
        assertEquals(product.getName(), result.name());
        assertEquals(product.getDescription(), result.description());
        assertEquals(product.isActive(), result.isActive());
        assertEquals(product.getPrice(), result.price());
        assertEquals(product.getQuantity(), result.quantity());
    }

    @Test
    void shouldCallCategoryFindByIdWhenCategoryUpdated() {
        ProductRegister data = buildProductRegisterData();
        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.of(category));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.existsByNameIgnoreCase(data.name())).thenReturn(false);
        when(productMapper.getEntity(data)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.getDetails(product)).thenReturn(buildProductDetails());

        service.update(product.getId(), data);

        verify(categoryRepository, never()).findById(data.categoryId());

        Category otherCategory = new Category(
                category.getId() + 1,
                category.getName(),
                category.getDescription(),
                category.isActive()
        );
        product.setCategory(otherCategory);

        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.of(otherCategory));

        service.update(product.getId(), data);

        verify(categoryRepository, times(1)).findById(data.categoryId());
    }

    @Test
    void shouldThrowExceptionWhenUpdateProductNotFound() {
        ProductRegister data = buildProductRegisterData();
        when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class, () -> service.update(product.getId(), data)
        );

        assertEquals("Product " + product.getId() + " not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithCategoryNotActive() {
        ProductRegister data = buildProductRegisterData();
        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.of(category));
        category.setActive(false);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.update(product.getId(), data)
        );

        assertEquals("invalid_value", exception.getType());
        assertEquals("categoryId", exception.getField());
        assertEquals("Category not active", exception.getMessage());
        assertEquals(data.categoryId(), exception.getValue());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithCategoryNotFound() {
        ProductRegister data = buildProductRegisterData();
        product.setCategory(new Category(data.categoryId() + 1, "", "", true));
        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.empty());
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.update(product.getId(), data)
        );

        assertEquals("invalid_value", exception.getType());
        assertEquals("categoryId", exception.getField());
        assertEquals("Category not found", exception.getMessage());
        assertEquals(data.categoryId(), exception.getValue());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithProductNameAlreadyExists() {
        ProductRegister data = buildProductRegisterData();
        when(categoryRepository.findById(data.categoryId())).thenReturn(Optional.of(category));
        when(productRepository.existsByNameIgnoreCaseAndIdNot(data.name(), product.getId())).thenReturn(true);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.update(product.getId(), data)
        );

        assertEquals("already_exists", exception.getType());
        assertEquals("name", exception.getField());
        assertEquals("Name already exists", exception.getMessage());
        assertEquals(data.name(), exception.getValue());
    }

    @Test
    void shouldDeleteByIdProduct() {
        doNothing().when(productRepository).deleteById(product.getId());
        service.deleteById(product.getId());
        verify(productRepository, times(1)).deleteById(product.getId());
    }

    private ProductRegister buildProductRegisterData() {
        return new ProductRegister(
                "Product 1",
                "Product description",
                true,
                19.9,
                10,
                category.getId()
        );
    }

    private ProductDetails buildProductDetails() {
        return new ProductDetails(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.isActive(),
                product.getPrice(),
                product.getQuantity(),
                new CategoryInfo(
                        category.getId(),
                        category.getName(),
                        category.isActive()
                )
        );
    }
}
