package com.example.fakeestore.exceptions;

public class UserNameNotFoundException extends RuntimeException {

    private String userName;
    public UserNameNotFoundException(String userName) {
        super("User with given username doesn't exist:" + userName);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
