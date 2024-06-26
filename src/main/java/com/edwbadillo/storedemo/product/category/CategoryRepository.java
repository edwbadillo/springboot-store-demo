package com.edwbadillo.storedemo.product.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Category}.
 *
 * @author edwbadillo
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);

    @Query("SELECT CASE WHEN COUNT(p.id) > 0 THEN true ELSE false END FROM Product p WHERE p.category.id = :id")
    boolean hasProducts(Integer id);
}
