package com.example.fakeebay.controller;

import com.example.fakeebay.dto.ProductDto;
import com.example.fakeebay.entity.Product;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.ProductNotFoundException;
import com.example.fakeebay.exceptions.UserIdNotFoundException;
import com.example.fakeebay.mapper.ProductMapper;
import com.example.fakeebay.messages.ErrorMessage;
import com.example.fakeebay.messages.ResponseMessage;
import com.example.fakeebay.service.ProductService;
import com.example.fakeebay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductMapper productMapper;


    @PostMapping()
    public ResponseEntity createProduct(@RequestBody ProductDto productDto)
    {
        try
        {
            Product product = productMapper.convertDtoToEntity(productDto);
            return new ResponseEntity(productMapper.convertEntityToDto(productService.createProduct(product)), HttpStatus.CREATED);
        }catch(UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("USER_NOT_FOUND_EXCEPTION",e.getMessage(),e.getId()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/search")
    public ResponseEntity searchText(@RequestParam(name = "text") String text)
    {
        return new ResponseEntity(productService.getProductsByText(text).stream().map(productMapper::convertEntityToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/advancedSearch")
    public ResponseEntity searchFiltered(@RequestParam(required = false, defaultValue = "") String name,
                                         @RequestParam(required = false, defaultValue = "") String barcode,
                                         @RequestParam(name = "quantity", required = false) Integer qty,
                                         @RequestParam(required = false) Float minPrice,
                                         @RequestParam(required = false) Float maxPrice)
    {
        return new ResponseEntity(productService.getFilteredProducts(name,barcode, qty,minPrice,maxPrice).stream().map(productMapper::convertEntityToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity updateProduct(@RequestBody ProductDto productDto)
    {
        try{
            Product product = productMapper.convertDtoToEntity(productDto);
            return new ResponseEntity(productMapper.convertEntityToDto(productService.updateProduct(product)), HttpStatus.OK);
        }catch(UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getId()),HttpStatus.OK);
        }
        catch (ProductNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_PRODUCT_NOT_FOUND",e.getMessage(),e.getId()),HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id)
    {
        try{
            productService.deleteProduct(id);
            return new ResponseEntity(new ResponseMessage("PRODUCT_DELETED"), HttpStatus.OK);
        }catch (ProductNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_PRODUCT_NOT_FOUND", e.getMessage(),e.getId()),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity getAllProducts()
    {
        return new ResponseEntity(productService.getAllProducts().stream().map(productMapper::convertEntityToDto), HttpStatus.OK);
    }

    @GetMapping(path = "/available")
    public ResponseEntity getAllAvailableProducts()
    {

        return new ResponseEntity(productService.getAllAvailableProducts().stream().map(productMapper::convertEntityToDto), HttpStatus.OK);
    }

    @GetMapping(path = "/user/{id}")
    public ResponseEntity getUserProducts(@PathVariable Long id)
    {
        try
        {
            User user = userService.getUserById(id);
            return new ResponseEntity(productService.getProductsByUser(user).stream().map(productMapper::convertEntityToDto), HttpStatus.OK);
        }catch(UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(), e.getId()), HttpStatus.BAD_REQUEST);
        }
    }




}
