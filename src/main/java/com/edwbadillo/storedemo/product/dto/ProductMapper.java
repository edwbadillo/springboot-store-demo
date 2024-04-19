package com.edwbadillo.storedemo.product.dto;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.product.Product;
import com.edwbadillo.storedemo.product.category.dto.CategoryMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Defines a mapper for {@link Product} and DTOs.
 *
 * @author edwbadillo
 */
@Service
public class ProductMapper {

    private final CategoryMapper categoryMapper;

    public ProductMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public PageDTO<ProductInfo> getPage(Page<Product> page) {
        return new PageDTO<>(
                page.getContent().stream().map(this::getInfo).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getNumberOfElements()
        );
    }

    public Product getEntity(ProductRegister data) {
        Product product = new Product();
        product.setName(data.name());
        product.setDescription(data.description());
        product.setActive(data.isActive());
        product.setPrice(data.price());
        product.setQuantity(data.quantity());

        // Category must be an entity from the database

        return product;
    }


    public ProductDetails getDetails(Product product) {
        return new ProductDetails(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.isActive(),
            product.getPrice(),
            product.getQuantity(),
            categoryMapper.getInfo(product.getCategory())
        );
    }


    public ProductInfo getInfo(Product product) {
        return new ProductInfo(
            product.getId(),
            product.getName(),
            product.isActive(),
            product.getPrice(),
            product.getQuantity()
        );
    }


    public void updateEntity(ProductRegister data, Product product) {
        product.setName(data.name());
        product.setDescription(data.description());
        product.setActive(data.isActive());
        product.setPrice(data.price());
        product.setQuantity(data.quantity());
    }
}
