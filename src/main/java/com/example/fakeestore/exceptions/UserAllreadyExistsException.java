package com.example.fakeestore.exceptions;

import com.example.fakeestore.entity.User;

public class UserAllreadyExistsException extends Throwable{

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
