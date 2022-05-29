package com.example.fakeestore.controller;

import com.example.fakeestore.entity.Category;
import com.example.fakeestore.exceptions.CategoryAllreadyExistsException;
import com.example.fakeestore.messages.ErrorMessage;
import com.example.fakeestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "category")
public class CategoryController {

    @Autowired
    ProductService productService;

    @PostMapping()
    public ResponseEntity createCategory(@RequestBody Category category)
    {
        try
        {
            return new ResponseEntity(productService.createCategory(category), HttpStatus.CREATED);
        }catch(CategoryAllreadyExistsException e)
        {
            return new ResponseEntity(new ErrorMessage("CATEGORY_ALLREADY_EXISTS_EXCEPTION",e.getMessage(),e.getCategory()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping()
    public ResponseEntity getCategories()
    {
        return new ResponseEntity(productService.getCategories(), HttpStatus.OK);
    }



}
