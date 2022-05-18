package com.example.fakeestore.exceptions;

import com.example.fakeestore.entity.Address;

public class AddressNotFoundException extends RuntimeException {
    private Long id;
    public AddressNotFoundException(Long id) {

        super("Address with given id not found: " + id);
        this.id = id;
    }

    public Long getAddress() {
        return id;
    }
}
