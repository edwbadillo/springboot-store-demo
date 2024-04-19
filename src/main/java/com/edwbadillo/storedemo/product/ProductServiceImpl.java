package com.edwbadillo.storedemo.product;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.exception.InvalidDataException;
import com.edwbadillo.storedemo.product.category.Category;
import com.edwbadillo.storedemo.product.category.CategoryRepository;
import com.edwbadillo.storedemo.product.dto.ProductDetails;
import com.edwbadillo.storedemo.product.dto.ProductInfo;
import com.edwbadillo.storedemo.product.dto.ProductMapper;
import com.edwbadillo.storedemo.product.dto.ProductRegister;
import com.edwbadillo.storedemo.product.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The {@link ProductService} implementation for managing {@link Product} objects.
 *
 * @author edwbadillo
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public PageDTO<ProductInfo> paginate(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);
        return productMapper.getPage(page);
    }

    @Override
    public ProductDetails getById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product " + id + " not found")
        );
        return productMapper.getDetails(product);
    }

    @Override
    public ProductDetails create(ProductRegister data) {
        Optional<Category> categoryResult = categoryRepository.findById(data.categoryId());
        if (categoryResult.isEmpty()) {
            throw new InvalidDataException("invalid_value", "categoryId", "Category not found", data.categoryId());
        }

        Category category = categoryResult.get();
        if (!category.isActive()) {
            throw new InvalidDataException("invalid_value", "categoryId", "Category not active", data.categoryId());
        }

        if (productRepository.existsByNameIgnoreCase(data.name())) {
            throw new InvalidDataException("already_exists", "name", "Name already exists", data.name());
        }

        Product product = productMapper.getEntity(data);
        product.setCategory(category);
        productRepository.save(product);
        return productMapper.getDetails(product);
    }

    @Override
    public ProductDetails update(Integer id, ProductRegister data) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Product " + id + " not found")
        );

        Category category;
        if (!product.getCategory().getId().equals(data.categoryId())) {
            // Category changed
            Optional<Category> categoryResult = categoryRepository.findById(data.categoryId());
            if (categoryResult.isEmpty()) {
                throw new InvalidDataException("invalid_value", "categoryId", "Category not found", data.categoryId());
            }
            category = categoryResult.get();
        } else {
            category = product.getCategory();
        }

        if (!category.isActive()) {
            throw new InvalidDataException("invalid_value", "categoryId", "Category not active", data.categoryId());
        }

        if (productRepository.existsByNameIgnoreCaseAndIdNot(data.name(), id)) {
            throw new InvalidDataException("already_exists", "name", "Name already exists", data.name());
        }

        productMapper.updateEntity(data, product);
        return productMapper.getDetails(productRepository.save(product));
    }


    @Override
    public void deleteById(Integer id) {
        // TODO: Check if product is in use or add soft delete
        productRepository.deleteById(id);
    }

    // Helpers
    private Category validateCategory(ProductRegister data) {
        Optional<Category> categoryResult = categoryRepository.findById(data.categoryId());
        if (categoryResult.isEmpty()) {
            throw new InvalidDataException("invalid_value", "categoryId", "Category not found", data.categoryId());
        }

        Category category = categoryResult.get();
        if (!category.isActive()) {
            throw new InvalidDataException("invalid_value", "categoryId", "Category not active", data.categoryId());
        }
        return category;
    }
}
