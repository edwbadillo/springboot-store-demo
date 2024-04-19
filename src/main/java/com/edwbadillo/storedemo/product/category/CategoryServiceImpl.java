package com.edwbadillo.storedemo.product.category;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.exception.InvalidDataException;
import com.edwbadillo.storedemo.product.category.dto.CategoryData;
import com.edwbadillo.storedemo.product.category.dto.CategoryDetails;
import com.edwbadillo.storedemo.product.category.dto.CategoryInfo;
import com.edwbadillo.storedemo.product.category.dto.CategoryMapper;
import com.edwbadillo.storedemo.product.category.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Implementation of {@link CategoryService}.
 *
 * @author edwbadillo
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageDTO<CategoryInfo> paginate(Pageable pageable) {
        Page<Category> page = repository.findAll(pageable);
        return categoryMapper.getPage(page);
    }

    @Override
    public CategoryDetails getById(Integer id) {
        Category category = repository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category " + id + " not found")
        );
        return categoryMapper.getDetails(category);
    }

    @Override
    public CategoryDetails create(CategoryData data) {
        if (repository.existsByNameIgnoreCase(data.name())) {
            throw new InvalidDataException("already_exists", "name", "Name already exists", data.name());
        }
        Category category = repository.save(categoryMapper.getEntity(data));
        return categoryMapper.getDetails(category);
    }

    @Override
    public CategoryDetails update(Integer id, CategoryData data) {
        if (repository.existsByNameIgnoreCaseAndIdNot(data.name(), id)) {
            throw new InvalidDataException("already_exists", "name", "Name already exists", data.name());
        }
        Category category = repository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category " + id + " not found")
        );
        categoryMapper.updateEntity(data, category);
        return categoryMapper.getDetails(repository.save(category));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
