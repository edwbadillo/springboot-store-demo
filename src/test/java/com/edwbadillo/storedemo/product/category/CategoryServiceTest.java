package com.edwbadillo.storedemo.product.category;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.exception.InvalidDataException;
import com.edwbadillo.storedemo.product.category.dto.CategoryData;
import com.edwbadillo.storedemo.product.category.dto.CategoryDetails;
import com.edwbadillo.storedemo.product.category.dto.CategoryInfo;
import com.edwbadillo.storedemo.product.category.dto.CategoryMapper;
import com.edwbadillo.storedemo.product.category.exception.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService service;

    @MockBean
    private CategoryRepository repository;

    @MockBean
    private CategoryMapper mapper;

    private Category category;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        category = new Category();
        category.setId(1);
        category.setName("smartphone");
        category.setDescription("Smartphone category description");
        category.setActive(true);
    }


    @Test
    void shouldPaginateCategories() {
        Page<Category> page = new PageImpl<>(List.of(category));
        Pageable pageable = Pageable.ofSize(10).withPage(0);

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.getPage(page)).thenReturn(new PageDTO<>(
                List.of(
                        new CategoryInfo(
                                category.getId(),
                                category.getName(),
                                category.isActive()
                        )
                ),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getNumberOfElements()
        ));

        PageDTO<CategoryInfo> result = service.paginate(pageable);

        assertEquals(page.getTotalElements(), result.totalCount());
        assertEquals(page.getTotalPages(), result.totalPages());
        assertEquals(page.getNumber(), result.numPage());
        assertEquals(page.getNumberOfElements(), result.numItems());

        assertEquals(category.getId(), result.items().get(0).id());
        assertEquals(category.getName(), result.items().get(0).name());
        assertEquals(category.isActive(), result.items().get(0).isActive());
    }

    @Test
    void shouldGetCategoryById() {
        when(repository.findById(category.getId())).thenReturn(Optional.of(category));
        when(mapper.getDetails(category)).thenReturn(
                new CategoryDetails(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        category.isActive()
                ));

        CategoryDetails result = service.getById(category.getId());

        assertEquals(category.getId(), result.id());
        assertEquals(category.getName(), result.name());
        assertEquals(category.getDescription(), result.description());
        assertEquals(category.isActive(), result.isActive());
    }

    @Test
    void shouldThrowExceptionWhenGetCategoryByIdNotFound() {
        when(repository.findById(category.getId())).thenReturn(Optional.empty());
        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class, () -> service.getById(category.getId())
        );

        assertEquals("Category " + category.getId() + " not found", exception.getMessage());
    }

    @Test
    void shouldCreateCategory() {
        CategoryData data = new CategoryData(
                "smartphone",
                "Smartphone category description",
                true
        );

        when(repository.existsByNameIgnoreCase(data.name())).thenReturn(false);
        when(mapper.getEntity(data)).thenReturn(category);
        when(repository.save(category)).thenReturn(category);
        when(mapper.getDetails(category)).thenReturn(
                new CategoryDetails(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        category.isActive()
                ));

        CategoryDetails categoryDetails = service.create(data);

        assertEquals(1, categoryDetails.id());
        assertEquals(category.getName(), categoryDetails.name());
        assertEquals(category.getDescription(), categoryDetails.description());
        assertEquals(category.isActive(), categoryDetails.isActive());
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExistsByName() {
        CategoryData data = new CategoryData(
                "smartphone",
                "Smartphone category description",
                true
        );

        when(repository.existsByNameIgnoreCase(data.name())).thenReturn(true);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.create(data)
        );

        assertEquals("already_exists", exception.getType());
        assertEquals("name", exception.getField());
        assertEquals("Name already exists", exception.getMessage());
        assertEquals(data.name(), exception.getValue());
    }

    @Test
    void shouldUpdateCategory() {
        CategoryData data = new CategoryData(
                "smartphone updated",
                "Smartphone category description updated",
                true
        );

        when(repository.findById(category.getId())).thenReturn(Optional.of(category));
        when(repository.existsByNameIgnoreCaseAndIdNot(data.name(), category.getId())).thenReturn(false);
        when(mapper.getEntity(data)).thenReturn(category);
        when(repository.save(category)).thenReturn(category);
        when(mapper.getDetails(category)).thenReturn(
                new CategoryDetails(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        category.isActive()
                ));
        doNothing().when(mapper).updateEntity(data, category);

        CategoryDetails categoryDetails = service.update(category.getId(), data);

        verify(mapper, times(1)).updateEntity(data, category);
        assertEquals(1, categoryDetails.id());
        assertEquals(category.getName(), categoryDetails.name());
        assertEquals(category.getDescription(), categoryDetails.description());
        assertEquals(category.isActive(), categoryDetails.isActive());
    }

    @Test
    void shouldThrowExceptionWhenUpdateCategoryAlreadyExistsByName() {
        CategoryData data = new CategoryData(
                "smartphone updated",
                "Smartphone category description updated",
                true
        );

        when(repository.existsByNameIgnoreCaseAndIdNot(data.name(), category.getId())).thenReturn(true);

        InvalidDataException exception = assertThrows(
                InvalidDataException.class, () -> service.update(category.getId(), data)
        );

        assertEquals("already_exists", exception.getType());
        assertEquals("name", exception.getField());
        assertEquals("Name already exists", exception.getMessage());
        assertEquals(data.name(), exception.getValue());
    }

    @Test
    void shouldThrowExceptionWhenCategoryDoesNotExist() {
        CategoryData data = new CategoryData(
                "smartphone updated",
                "Smartphone category description updated",
                true
        );

        when(repository.findById(category.getId())).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class, () -> service.update(category.getId(), data)
        );

        assertEquals("Category " + category.getId() + " not found", exception.getMessage());
    }

    @Test
    void shouldDeleteCategory() {
        doNothing().when(repository).deleteById(category.getId());
        service.deleteById(category.getId());
        verify(repository, times(1)).deleteById(category.getId());
    }

}
