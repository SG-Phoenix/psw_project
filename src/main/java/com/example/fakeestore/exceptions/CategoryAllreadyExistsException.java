package com.example.fakeestore.exceptions;

public class CategoryAllreadyExistsException extends Throwable {
    private String name;
    public CategoryAllreadyExistsException(String name) {

        super("Category with given name allready exists " + name);
        this.name = name;
    }

    public String getCategory() {
        return this.name;
    }
}
