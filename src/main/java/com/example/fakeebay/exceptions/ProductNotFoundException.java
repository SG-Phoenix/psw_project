package com.example.fakeebay.exceptions;

import com.example.fakeebay.entity.Product;

public class ProductNotFoundException extends RuntimeException {
    private Long id;
    public ProductNotFoundException(Long id) {
        super("Product with id " + id + " not found");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
