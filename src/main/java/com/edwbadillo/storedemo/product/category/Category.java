package com.edwbadillo.storedemo.product.category;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Defines the category of a product.
 *
 * @author edwbadillo
 */
@Entity
@Table(name = "product_category")
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private boolean isActive = true;
}
