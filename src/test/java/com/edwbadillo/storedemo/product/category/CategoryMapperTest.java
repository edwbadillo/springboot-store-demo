package com.edwbadillo.storedemo.product.category;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.product.category.dto.CategoryData;
import com.edwbadillo.storedemo.product.category.dto.CategoryDetails;
import com.edwbadillo.storedemo.product.category.dto.CategoryInfo;
import com.edwbadillo.storedemo.product.category.dto.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryMapperTest {

    private CategoryMapper categoryMapper;

    private Category category;

    @BeforeEach
    void setup() {
        categoryMapper = new CategoryMapper();

        category = new Category(1, "Smartphone", "This is a test description", true);
    }

    @Test
    void shouldMapCategoryPageToCategoryInfoPageDTO() {
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        PageDTO<CategoryInfo> categoryInfoPage = categoryMapper.getPage(categoryPage);

        assertEquals(categoryPage.getTotalElements(), categoryInfoPage.totalCount());
        assertEquals(categoryPage.getTotalPages(), categoryInfoPage.totalPages());
        assertEquals(categoryPage.getNumber(), categoryInfoPage.numPage());
        assertEquals(categoryPage.getNumberOfElements(), categoryInfoPage.numItems());

        assertEquals(category.getId(), categoryInfoPage.items().get(0).id());
        assertEquals(category.getName(), categoryInfoPage.items().get(0).name());
        assertEquals(category.isActive(), categoryInfoPage.items().get(0).isActive());
    }

    @Test
    void shouldMapCategoryDataToEntity() {
        CategoryData categoryData = new CategoryData(
                "Smartphone",
                "This is a test description",
                true
        );

        Category category = categoryMapper.getEntity(categoryData);

        assertEquals(categoryData.name(), category.getName());
        assertEquals(categoryData.description(), category.getDescription());
        assertEquals(categoryData.isActive(), category.isActive());
    }

    @Test
    void shouldMapCategoryEntityToDetails() {
        CategoryDetails categoryDetails = categoryMapper.getDetails(category);

        assertEquals(category.getId(), categoryDetails.id());
        assertEquals(category.getName(), categoryDetails.name());
        assertEquals(category.getDescription(), categoryDetails.description());
        assertEquals(category.isActive(), categoryDetails.isActive());
    }

    @Test
    void shouldMapCategoryEntityToInfo() {
        CategoryInfo categoryInfo = categoryMapper.getInfo(category);

        assertEquals(category.getId(), categoryInfo.id());
        assertEquals(category.getName(), categoryInfo.name());
        assertEquals(category.isActive(), categoryInfo.isActive());
    }

    @Test
    void shouldUpdateCategoryEntity() {
        CategoryData categoryData = new CategoryData(
                "Smartphone",
                "This is a test description",
                true
        );

        Category categoryEntity = new Category();

        categoryMapper.updateEntity(categoryData, categoryEntity);

        assertEquals(categoryData.name(), categoryEntity.getName());
        assertEquals(categoryData.description(), categoryEntity.getDescription());
        assertEquals(categoryData.isActive(), categoryEntity.isActive());
    }
}
