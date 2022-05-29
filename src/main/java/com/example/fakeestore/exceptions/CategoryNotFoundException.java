package com.example.fakeestore.exceptions;

public class CategoryNotFoundException extends Throwable {
    private String name;

    public CategoryNotFoundException(String name) {

        super("Category with given name not found " + name);
        this.name = name;
    }

    public String getCategory() {
        return this.name;
    }

}
