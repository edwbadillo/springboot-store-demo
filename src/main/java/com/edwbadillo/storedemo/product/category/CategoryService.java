package com.edwbadillo.storedemo.product.category;


import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.exception.InvalidDataException;
import com.edwbadillo.storedemo.product.category.dto.CategoryData;
import com.edwbadillo.storedemo.product.category.dto.CategoryDetails;
import com.edwbadillo.storedemo.product.category.dto.CategoryInfo;
import org.springframework.data.domain.Pageable;


/**
 * Defines a service for managing {@link Category} objects.
 *
 * @author edwbadillo
 */
public interface CategoryService {

    /**
     * Returns a {@link PageDTO} with {@link CategoryInfo} objects.
     *
     * @param pageable a {@link Pageable} object with pagination information
     * @return {@link PageDTO}
     */
    PageDTO<CategoryInfo> paginate(Pageable pageable);

    /**
     * Finds a category by its id.
     *
     * @param id the id to find
     * @return {@link CategoryDetails}
     */
    CategoryDetails getById(Integer id);

    /**
     * Creates a new category.
     *
     * @param data {@link CategoryData}
     * @return {@link CategoryDetails}
     * @throws InvalidDataException if the category already exists by name
     */
    CategoryDetails create(CategoryData data);

    /**
     * Updates an existing category.
     *
     * @param data {@link CategoryData}
     * @param id the id of the category to update
     * @return {@link CategoryDetails}
     * @throws InvalidDataException if the category already exists by name
     */
    CategoryDetails update(CategoryData data, Integer id);

    /**
     * Deletes an existing category.
     *
     * @param id the id of the category to delete
     */
    void deleteById(Integer id);
}
