package com.example.fakeestore.exceptions;

import com.example.fakeestore.entity.Product;

public class ProductChangedPrice extends Throwable {

    private Product product;
    public ProductChangedPrice(Product product) {
        super("Product with id " + product.getId() + " changed price" );
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
