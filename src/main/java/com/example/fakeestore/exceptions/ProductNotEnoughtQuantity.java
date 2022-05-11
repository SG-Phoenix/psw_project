package com.example.fakeestore.exceptions;

import com.example.fakeestore.entity.Product;

public class ProductNotEnoughtQuantity extends RuntimeException {
    private Product product;
    private int quantity;
    public ProductNotEnoughtQuantity(Product product, int quantity) {
        super("Product with id " + product.getId() + " has not enought quantity: " + quantity + " ( only " + product.getQuantity() + " available )" );
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
