package com.example.fakeestore.exceptions;

public class OrderNotFoundException extends Throwable {
    private Long id;
    public OrderNotFoundException(Long id) {
        super("Order with given id not found: "+ id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
