package com.edwbadillo.storedemo.common;

import java.util.List;

/**
 * Represents a page items, this is used for returning items from a paginated
 * query in controllers or services.
 *
 * @param <T> the type of items in the page
 * @author edwbadillo
 */
public record PageDTO<T>(
        List<T> items,
        long totalCount,
        int totalPages,
        int numPage,
        int numItems
) { }
