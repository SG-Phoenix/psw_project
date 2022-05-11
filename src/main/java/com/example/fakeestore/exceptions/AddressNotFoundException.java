package com.example.fakeestore.exceptions;

import com.example.fakeestore.entity.Address;

public class AddressNotFoundException extends RuntimeException {
    private Address address;
    public AddressNotFoundException(Address address) {

        super("Address with given id not found: " + address.getId());
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }
}
