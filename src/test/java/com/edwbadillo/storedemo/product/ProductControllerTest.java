package com.edwbadillo.storedemo.product;

import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.product.category.dto.CategoryInfo;
import com.edwbadillo.storedemo.product.dto.ProductDetails;
import com.edwbadillo.storedemo.product.dto.ProductInfo;
import com.edwbadillo.storedemo.product.dto.ProductRegister;
import com.edwbadillo.storedemo.product.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService employeeService;

    @Test
    void shouldPaginateProducts() throws Exception {
        ProductInfo product = new ProductInfo(1, "Product", true, 1.0, 1);

        when(employeeService.paginate(any(Pageable.class))).thenReturn(new PageDTO<>(
                List.of(product),
                1,
                1,
                1,
                1
        ));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(product.id()))
                .andExpect(jsonPath("$.items[0].name").value(product.name()))
                .andExpect(jsonPath("$.items[0].isActive").value(product.isActive()))
                .andExpect(jsonPath("$.items[0].price").value(product.price()))
                .andExpect(jsonPath("$.items[0].quantity").value(product.quantity()))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.numPage").value(1))
                .andExpect(jsonPath("$.numItems").value(1));
    }

    @Test
    void shouldGetProductById() throws Exception {
        ProductDetails product = buildProductDetails();

        when(employeeService.getById(1)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.id()))
                .andExpect(jsonPath("$.name").value(product.name()))
                .andExpect(jsonPath("$.description").value(product.description()))
                .andExpect(jsonPath("$.isActive").value(product.isActive()))
                .andExpect(jsonPath("$.price").value(product.price()))
                .andExpect(jsonPath("$.quantity").value(product.quantity()))
                .andExpect(jsonPath("$.category.id").value(product.category().id()))
                .andExpect(jsonPath("$.category.name").value(product.category().name()))
                .andExpect(jsonPath("$.category.isActive").value(product.category().isActive()));
    }

    @Test
    void shouldGet404WhenProductNotFoundById() throws Exception {
        when(employeeService.getById(1))
                .thenThrow(new ProductNotFoundException("Product 1 not found"));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product 1 not found"));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRegister data = buildRegisterData();
        ProductDetails details = buildProductDetails();

        when(employeeService.create(any(ProductRegister.class))).thenReturn(details);

        String jsonData = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(
                    post("/api/products")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(details.id()))
                .andExpect(jsonPath("$.name").value(details.name()))
                .andExpect(jsonPath("$.description").value(details.description()))
                .andExpect(jsonPath("$.isActive").value(details.isActive()))
                .andExpect(jsonPath("$.price").value(details.price()))
                .andExpect(jsonPath("$.quantity").value(details.quantity()))
                .andExpect(jsonPath("$.category.id").value(details.category().id()))
                .andExpect(jsonPath("$.category.name").value(details.category().name()))
                .andExpect(jsonPath("$.category.isActive").value(details.category().isActive()));
    }

    @Test
    void shouldPositiveProductPrice() throws Exception {
        ProductRegister data = new ProductRegister(
                "Product",
                "Description",
                true,
                -1.0,
                1,
                1
        );

        String jsonData = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(
                    post("/api/products")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request body, check errors"))
                .andExpect(jsonPath("$.errors[0].type").value("DecimalMin"))
                .andExpect(jsonPath("$.errors[0].message").value("The price must be greater than or equal to 0"))
                .andExpect(jsonPath("$.errors[0].field").value("price"))
                .andExpect(jsonPath("$.errors[0].value").value(-1.0));
    }

    @Test
    void shouldPositiveProductQuantity() throws Exception {
        ProductRegister data = new ProductRegister(
                "Product",
                "Description",
                true,
                19.9,
                -1,
                1
        );

        String jsonData = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(
                        post("/api/products")
                                .content(jsonData)
                                .contentType("application/json")
                                .accept("application/json")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid request body, check errors"))
                .andExpect(jsonPath("$.errors[0].type").value("Min"))
                .andExpect(jsonPath("$.errors[0].message").value("The quantity must be greater than or equal to 0"))
                .andExpect(jsonPath("$.errors[0].field").value("quantity"))
                .andExpect(jsonPath("$.errors[0].value").value(-1));
    }


    @Test
    void shouldUpdateProduct() throws Exception {
        ProductRegister data = buildRegisterData();
        ProductDetails details = buildProductDetails();
        when(employeeService.update(anyInt(), any(ProductRegister.class))).thenReturn(details);

        String jsonData = new ObjectMapper().writeValueAsString(data);

        mockMvc.perform(
                    put("/api/products/1")
                        .content(jsonData)
                        .contentType("application/json")
                        .accept("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(details.id()))
                .andExpect(jsonPath("$.name").value(details.name()))
                .andExpect(jsonPath("$.description").value(details.description()))
                .andExpect(jsonPath("$.isActive").value(details.isActive()))
                .andExpect(jsonPath("$.price").value(details.price()))
                .andExpect(jsonPath("$.quantity").value(details.quantity()))
                .andExpect(jsonPath("$.category.id").value(details.category().id()))
                .andExpect(jsonPath("$.category.name").value(details.category().name()))
                .andExpect(jsonPath("$.category.isActive").value(details.category().isActive()));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        doNothing().when(employeeService).deleteById(1);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteById(1);
    }

    private ProductRegister buildRegisterData() {
        return new ProductRegister(
                "Product",
                "Description",
                true,
                19.9,
                1,
                1
        );
    }

    private ProductDetails buildProductDetails() {
        return new ProductDetails(
                1,
                "Product",
                "Description",
                true,
                19.9,
                1,
                new CategoryInfo(1, "Category", true)
        );
    }
}
