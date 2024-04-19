package com.edwbadillo.storedemo.product;

import com.edwbadillo.storedemo.product.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The product in the store belongs to a category.
 *
 * @author edwbadillo
 */
@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private boolean isActive = true;
    private double price;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = 0;
        this.quantity = 0;
    }
}
