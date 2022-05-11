package com.example.fakeestore.exceptions;

public class UserIdNotFoundException extends RuntimeException{

    private Long id;
    public UserIdNotFoundException(Long id)
    {
        super("User with given id not found: " + id );
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
