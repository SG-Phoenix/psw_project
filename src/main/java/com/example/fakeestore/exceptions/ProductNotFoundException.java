package com.example.fakeestore.exceptions;

public class ProductNotFoundException extends Throwable {
    private Long id;
    public ProductNotFoundException(Long id) {
        super("Product with id " + id + " not found");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
