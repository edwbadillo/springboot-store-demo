package com.edwbadillo.storedemo.product;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.product.category.Category;
import com.edwbadillo.storedemo.product.category.dto.CategoryMapper;
import com.edwbadillo.storedemo.product.dto.ProductDetails;
import com.edwbadillo.storedemo.product.dto.ProductInfo;
import com.edwbadillo.storedemo.product.dto.ProductMapper;
import com.edwbadillo.storedemo.product.dto.ProductRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProductMapperTest {

    private ProductMapper productMapper;
    private Product product;
    private Category category;


    @BeforeEach
    void setup() {
        productMapper = new ProductMapper(new CategoryMapper());
        category = new Category(1, "Category1", "This is a test description");
        product = new Product();
        product.setId(1);
        product.setName("Product1");
        product.setDescription("This is a test description");
        product.setPrice(100.0);
        product.setQuantity(10);
        product.setCategory(category);
    }

    @Test
    void shouldMapProductPageToProductInfoPageDTO() {
        Page<Product> productPage = new PageImpl<>(List.of(product));
        PageDTO<ProductInfo> productInfoPage = productMapper.getPage(productPage);

        assertEquals(productPage.getTotalElements(), productInfoPage.totalCount());
        assertEquals(productPage.getTotalPages(), productInfoPage.totalPages());
        assertEquals(productPage.getNumber(), productInfoPage.numPage());
        assertEquals(productPage.getNumberOfElements(), productInfoPage.numItems());

        assertEquals(productInfoPage.items().get(0).id(), product.getId());
        assertEquals(productInfoPage.items().get(0).name(), product.getName());
        assertEquals(productInfoPage.items().get(0).isActive(), product.isActive());
        assertEquals(productInfoPage.items().get(0).price(), product.getPrice());
        assertEquals(productInfoPage.items().get(0).quantity(), product.getQuantity());
    }

    @Test
    void shouldMapProductEntityToDetails() {
        ProductDetails productDetails = productMapper.getDetails(product);

        assertEquals(productDetails.id(), product.getId());
        assertEquals(productDetails.name(), product.getName());
        assertEquals(productDetails.description(), product.getDescription());
        assertEquals(productDetails.price(), product.getPrice());
        assertEquals(productDetails.quantity(), product.getQuantity());
        assertEquals(productDetails.category().id(), product.getCategory().getId());
        assertEquals(productDetails.category().name(), product.getCategory().getName());
        assertEquals(productDetails.category().isActive(), product.getCategory().isActive());
    }

    @Test
    void shouldMapProductEntityToInfo() {
        ProductInfo productInfo = productMapper.getInfo(product);

        assertEquals(productInfo.id(), product.getId());
        assertEquals(productInfo.name(), product.getName());
        assertEquals(productInfo.isActive(), product.isActive());
        assertEquals(productInfo.price(), product.getPrice());
        assertEquals(productInfo.quantity(), product.getQuantity());
    }

    @Test
    void shouldMapProductRegisterToEntity() {
        ProductRegister data = new ProductRegister(
            "Product1",
            "This is a test description",
            true,
            99.9,
            15,
            1
        );

        Product product = productMapper.getEntity(data);
        assertEquals(data.name(), product.getName());
        assertEquals(data.description(), product.getDescription());
        assertEquals(data.isActive(), product.isActive());
        assertEquals(data.price(), product.getPrice());
        assertEquals(data.quantity(), product.getQuantity());
        assertNull(product.getCategory());
    }

    @Test
    void shouldMapProductUpdateToEntity() {
        ProductRegister data = new ProductRegister(
            "Product2",
            "This is a test description 2",
            true,
            150.9,
            10,
            2
        );

        productMapper.updateEntity(data, product);
        assertEquals(product.getName(), data.name());
        assertEquals(product.getDescription(), data.description());
        assertEquals(product.isActive(), data.isActive());
        assertEquals(product.getPrice(), data.price());
        assertEquals(product.getQuantity(), data.quantity());
        assertEquals(product.getCategory(), category);
    }
}
