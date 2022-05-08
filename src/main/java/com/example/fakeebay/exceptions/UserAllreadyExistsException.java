package com.example.fakeebay.exceptions;

import com.example.fakeebay.entity.User;

public class UserAllreadyExistsException extends RuntimeException{

    private User user;
    public UserAllreadyExistsException(User user)
    {
        super("Given user allready exists");
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
