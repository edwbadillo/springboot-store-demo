package com.edwbadillo.storedemo.product;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.product.dto.ProductDetails;
import com.edwbadillo.storedemo.product.dto.ProductInfo;
import com.edwbadillo.storedemo.product.dto.ProductRegister;
import org.springframework.data.domain.Pageable;

/**
 * The service for managing {@link Product} objects.
 *
 * @author edwbadillo
 */
public interface ProductService {

    /**
     * Paginates and returns a list of the products.
     *
     * @param pageable the pagination information
     * @return a {@link PageDTO} of the {@link ProductInfo} objects
     */
    PageDTO<ProductInfo> paginate(Pageable pageable);

    /**
     * Gets a product by its id.
     *
     * @param id the id of the product, must be present
     * @return the product
     */
    ProductDetails getById(Integer id);

    /**
     * Creates a new product.
     *
     * @param data the product data
     * @return the created product
     */
    ProductDetails create(ProductRegister data);

    /**
     * Updates an existing product.
     *
     * @param id the id of the product, must be present
     * @param data the product data
     * @return the updated product
     */
    ProductDetails update(Integer id, ProductRegister data);

    /**
     * Deletes a product by its id.
     *
     * @param id the id of the product
     */
    void deleteById(Integer id);
}
