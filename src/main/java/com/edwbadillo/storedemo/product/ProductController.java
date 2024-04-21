package com.edwbadillo.storedemo.product;

import com.edwbadillo.storedemo.common.InvalidField;
import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.common.SimpleMessageResponse;
import com.edwbadillo.storedemo.product.dto.ProductDetails;
import com.edwbadillo.storedemo.product.dto.ProductInfo;
import com.edwbadillo.storedemo.product.dto.ProductRegister;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Defines the endpoints for managing product resources.
 *
 * @author edwbadillo
 */
@Tag(name = "Product", description = "Product management APIs")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Paginate products", description = "Get a slice of products (paginated).")
    @GetMapping
    public PageDTO<ProductInfo> paginate(@ParameterObject Pageable pageable) {
        return productService.paginate(pageable);
    }

    @Operation(summary = "Get product", description = "Get a existing product by its ID.")
    @GetMapping("/{id}")
    public ProductDetails findById(@PathVariable Integer id) {
        return productService.getById(id);
    }

    @Operation(summary = "Create product", description = "Create a new product with the given data.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created",
                    content = { @Content(schema = @Schema(implementation = ProductDetails.class), mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid product data" ,
                    content = { @Content(schema = @Schema(implementation = InvalidField.class), mediaType = "application/json") }),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetails create(@Valid @RequestBody ProductRegister data) {
        return productService.create(data);
    }

    @Operation(summary = "Update product", description = "Update an existing product with the given data.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated",
                    content = { @Content(schema = @Schema(implementation = ProductDetails.class), mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid product data" ,
                    content = { @Content(schema = @Schema(implementation = InvalidField.class), mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found" ,
                    content = { @Content(schema = @Schema(implementation = SimpleMessageResponse.class), mediaType = "application/json") }),
    })
    @PutMapping("/{id}")
    public ProductDetails update(@PathVariable Integer id, @Valid @RequestBody ProductRegister productData) {
        return productService.update(id, productData);
    }

    @Operation(summary = "Delete product", description = "Delete a product, the product must not have any orders.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Product deleted",
                    content = { @Content(schema = @Schema(implementation = ProductDetails.class), mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "409",
                    description = "The product can't be deleted because it is in use" ,
                    content = { @Content(schema = @Schema(implementation = SimpleMessageResponse.class), mediaType = "application/json") }),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        productService.deleteById(id);
    }

}
