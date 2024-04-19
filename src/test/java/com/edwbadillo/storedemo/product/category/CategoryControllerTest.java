package com.edwbadillo.storedemo.product.category;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.exception.DataIntegrityException;
import com.edwbadillo.storedemo.exception.InvalidDataException;
import com.edwbadillo.storedemo.product.category.dto.CategoryData;
import com.edwbadillo.storedemo.product.category.dto.CategoryDetails;
import com.edwbadillo.storedemo.product.category.dto.CategoryInfo;
import com.edwbadillo.storedemo.product.category.exception.CategoryNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CategoryService employeeService;

    @Test
    void shouldPaginateCategories() throws Exception {
        CategoryInfo category = new CategoryInfo(
                1,
                "Smartphone",
                true
        );

        when(employeeService.paginate(any(Pageable.class))).thenReturn(new PageDTO<>(
                List.of(category),
                1,
                1,
                1,
                1
        ));

        mockMvc.perform(get("/api/products/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(category.id()))
                .andExpect(jsonPath("$.items[0].name").value(category.name()))
                .andExpect(jsonPath("$.items[0].isActive").value(category.isActive()))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.numPage").value(1))
                .andExpect(jsonPath("$.numItems").value(1));
    }

    @Test
    void shouldGetCategoryById() throws Exception {
        CategoryDetails category = new CategoryDetails(
                1,
                "Smartphone",
                "The smartphone category",
                true
        );

        when(employeeService.getById(1)).thenReturn(category);

        mockMvc.perform(get("/api/products/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.id()))
                .andExpect(jsonPath("$.name").value(category.name()))
                .andExpect(jsonPath("$.description").value(category.description()))
                .andExpect(jsonPath("$.isActive").value(category.isActive()));
    }

    @Test
    void shouldGetCategoryByIdNotFound() throws Exception {
        when(employeeService.getById(1)).thenThrow(new CategoryNotFoundException("Category 1 not found"));

        mockMvc.perform(get("/api/products/categories/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Category 1 not found"));
    }

    @Test
    void shouldCreateCategory() throws Exception {
        CategoryData data = new CategoryData(
                "Smartphone",
                "This is a test description",
                true
        );

        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 9, 5, 10);
        CategoryDetails categoryDetails = new CategoryDetails(
                1,
                "Smartphone",
                "This is a test description",
                true
        );

        when(employeeService.create(any(CategoryData.class))).thenReturn(categoryDetails);

        String jsonData = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(
                    post("/api/products/categories")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(categoryDetails.name()))
                .andExpect(jsonPath("$.description").value(categoryDetails.description()))
                .andExpect(jsonPath("$.isActive").value(categoryDetails.isActive()));
    }

    @Test
    void shouldGet400WhenNameExists() throws Exception {
        CategoryData data = new CategoryData(
                "Smartphone",
                "This is a test description",
                true
        );

        when(employeeService.create(any(CategoryData.class)))
                .thenThrow(new InvalidDataException(
                        "already_exists",
                        "name",
                        "Name already exists",
                        "Smartphone"
                ));

        String jsonData = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(
                    post("/api/products/categories")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("already_exists"))
                .andExpect(jsonPath("$.field").value("name"))
                .andExpect(jsonPath("$.message").value("Name already exists"))
                .andExpect(jsonPath("$.value").value("Smartphone"));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        CategoryData data = new CategoryData(
                "Smartphone",
                "This is a test description",
                true
        );

        CategoryDetails categoryDetails = new CategoryDetails(
                1,
                "Smartphone",
                "This is a test description",
                true
        );

        when(employeeService.update(any(CategoryData.class), anyInt())).thenReturn(categoryDetails);

        String jsonData = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(
                    put("/api/products/categories/1")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(categoryDetails.name()))
                .andExpect(jsonPath("$.description").value(categoryDetails.description()))
                .andExpect(jsonPath("$.isActive").value(categoryDetails.isActive()));
    }

    @Test
    void shouldGet400WhenNameExistsUpdate() throws Exception {
        CategoryData data = new CategoryData(
                "Smartphone",
                "This is a test description",
                true
        );

        when(employeeService.update(any(CategoryData.class), anyInt()))
                .thenThrow(new InvalidDataException(
                        "already_exists",
                        "name",
                        "Name already exists",
                        "Smartphone"
                ));

        String jsonData = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(
                    put("/api/products/categories/1")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("already_exists"))
                .andExpect(jsonPath("$.field").value("name"))
                .andExpect(jsonPath("$.message").value("Name already exists"))
                .andExpect(jsonPath("$.value").value("Smartphone"));
    }

    @Test
    void shouldNotFoundWhenUpdateCategoryDoesNotExist() throws Exception {
        CategoryData data = new CategoryData(
                "Smartphone",
                "This is a test description",
                true
        );

        when(employeeService.update(any(CategoryData.class), anyInt()))
                .thenThrow(new CategoryNotFoundException("Category 1 not found"));

        String jsonData = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(
                    put("/api/products/categories/1")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Category 1 not found"));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/products/categories/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteById(1);
    }

    @Test
    void shouldGetConflict409WhenCategoryHasProducts() throws Exception {
        doThrow(new DataIntegrityException("The category has products and cannot be deleted at the moment"))
                .when(employeeService).deleteById(1);

        mockMvc.perform(delete("/api/products/categories/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("The category has products and cannot be deleted at the moment"));
    }
}
