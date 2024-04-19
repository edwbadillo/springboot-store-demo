package com.edwbadillo.storedemo.product.category;

import com.edwbadillo.storedemo.common.InvalidDataResponse;
import com.edwbadillo.storedemo.common.PageDTO;
import com.edwbadillo.storedemo.common.SimpleMessageResponse;
import com.edwbadillo.storedemo.product.category.dto.CategoryData;
import com.edwbadillo.storedemo.product.category.dto.CategoryDetails;
import com.edwbadillo.storedemo.product.category.dto.CategoryInfo;
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
 * Defines the endpoints for managing {@link Category} resources.
 *
 * @author edwbadillo
 */
@Tag(name = "Product Category", description = "Category management APIs")
@RestController
@RequestMapping("/api/products/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @Operation(summary = "Paginate categories", description = "Get a slice of categories (paginated).")
    @GetMapping
    public PageDTO<CategoryInfo> paginate(@ParameterObject Pageable pageable) {
        return categoryService.paginate(pageable);
    }


    @Operation(summary = "Get category", description = "Get a existing category by its ID.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found" ,
                    content = { @Content(schema = @Schema(implementation = SimpleMessageResponse.class), mediaType = "application/json") }),
    })
    @GetMapping("/{id}")
    public CategoryDetails findById(@PathVariable Integer id) {
        return categoryService.getById(id);
    }


    @Operation(summary = "Create category", description = "Create a new category with the given data.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created",
                    content = { @Content(schema = @Schema(implementation = CategoryDetails.class), mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category data" ,
                    content = { @Content(schema = @Schema(implementation = InvalidDataResponse.class), mediaType = "application/json") }),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDetails create(@Valid @RequestBody CategoryData categoryData) {
        return categoryService.create(categoryData);
    }


    @Operation(summary = "Update category", description = "Update an existing category with the given data.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated",
                    content = { @Content(schema = @Schema(implementation = CategoryDetails.class), mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category data" ,
                    content = { @Content(schema = @Schema(implementation = InvalidDataResponse.class), mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found" ,
                    content = { @Content(schema = @Schema(implementation = SimpleMessageResponse.class), mediaType = "application/json") }),
    })
    @PutMapping("/{id}")
    public CategoryDetails update(@PathVariable Integer id, @Valid @RequestBody CategoryData categoryData) {
        return categoryService.update(id, categoryData);
    }


    @Operation(summary = "Delete category", description = "Delete a category, the category must not have any products.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Category deleted",
                    content = { @Content(schema = @Schema(implementation = CategoryDetails.class), mediaType = "application/json") }),
            @ApiResponse(
                    responseCode = "409",
                    description = "The category has products" ,
                    content = { @Content(schema = @Schema(implementation = SimpleMessageResponse.class), mediaType = "application/json") }),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        categoryService.deleteById(id);
    }
}
