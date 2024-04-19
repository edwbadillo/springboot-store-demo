package com.edwbadillo.storedemo.product.category.dto;



import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.product.category.Category;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

/**
 * Defines a mapper for {@link Category} and DTOs.
 *
 * @author edwbadillo
 */
@Service
public class CategoryMapper {

    public PageDTO<CategoryInfo> getPage(Page<Category> page) {
        return new PageDTO<>(
                page.getContent().stream().map(this::getInfo).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getNumberOfElements()
        );
    }

    public Category getEntity(CategoryData data) {
        Category category = new Category();
        category.setName(data.name());
        category.setDescription(data.description());
        category.setActive(data.isActive());
        return category;
    }

    public CategoryDetails getDetails(Category category) {
        return new CategoryDetails(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.isActive()
        );
    }

    public CategoryInfo getInfo(Category category) {
        return new CategoryInfo(
            category.getId(),
            category.getName(),
            category.isActive()
        );
    }

    public void updateEntity(CategoryData data, Category category) {
        category.setName(data.name());
        category.setDescription(data.description());
        category.setActive(data.isActive());
    }
}
