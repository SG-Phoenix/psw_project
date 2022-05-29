package com.example.fakeestore.controller;

import com.example.fakeestore.dto.OrderDto;
import com.example.fakeestore.dto.ProductDto;
import com.example.fakeestore.entity.Category;
import com.example.fakeestore.entity.Product;
import com.example.fakeestore.entity.User;
import com.example.fakeestore.exceptions.*;
import com.example.fakeestore.messages.ErrorMessage;
import com.example.fakeestore.messages.ResponseMessage;
import com.example.fakeestore.service.ProductService;
import com.example.fakeestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    /**
     *
     * Creates new product
     *
     * @param productDto                    creates a product from a valid dto object
        *                                   given as POST request body
     *
     * @return                              new product
     *
     * @throws UserIdNotFoundException      if user was not found by given id
     *
     * @see ProductDto
     *
     */

    @PostMapping()
    public ResponseEntity createProduct(@RequestBody ProductDto productDto)
    {
        try
        {
            User user = userService.getUserByUsername(productDto.getUserUsername());
            Category category = productService.getCategory(productDto.getCategoryName());
            Product product = modelMapper.map(productDto,  Product.class);
            product.setUser(user);
            product.setCategory(category);
            return new ResponseEntity(modelMapper.map(productService.createProduct(product), ProductDto.class), HttpStatus.CREATED);
        }
        catch (UserNameNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("USER_NOT_FOUND_EXCEPTION",e.getMessage(),e.getUserName()), HttpStatus.BAD_REQUEST);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("CATEGORY_NOT_FOUND_EXCEPTION",e.getMessage(),e.getCategory()), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Simple filter used to retrieve product by using simple string
     *
     * @param text      used to find products that contains given
     *                  string either in name or barcode
     *
     * @return          List of filtered products
     * @see ProductDto
     *
     */
    @GetMapping("/search/{text}")
    public ResponseEntity searchText(@PathVariable(name = "text") String text)
    {
        return new ResponseEntity(productService.getProductsByText(text).stream().map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Same as searchText but paged
     *
     * @param page          used to select desired page
     * @param pageSize      used to set single page dimension
     * @param sortBy        used to change sorting
     *
     * @return              List of filtered products
     *
     * @see ProductController#searchText(String)
     * @see ProductDto
     *
     */

    @GetMapping("/search/{text}/paged")
    public ResponseEntity searchTextPaged(@PathVariable(name = "text") String text,
                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
                                     )
    {
        return new ResponseEntity(productService.getProductsByText(text, page, pageSize, sortBy).stream().map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Advanced filter used to retrieve product by using multiple parameters
     *
     * @param name      used to find products that contains given name
     * @param barcode   used to find products that contains given barcode
     * @param qty       used to find products that have AT LEAST given quantity
     * @param minPrice  used to find products by price
     *                  retrives only those who have price > minPrice
     * @param maxPrice  used to find products by price
     *                  retrives only those who have price < maxPrice
     *
     * @return          List of filtered products
     *
     * @see ProductDto
     *
     */

    @GetMapping("/advancedSearch")
    public ResponseEntity searchFiltered(@RequestParam(required = false, defaultValue = "") String name,
                                         @RequestParam(required = false, defaultValue = "") String barcode,
                                         @RequestParam(name = "quantity", required = false) Integer qty,
                                         @RequestParam(required = false) Float minPrice,
                                         @RequestParam(required = false) Float maxPrice,
                                         @RequestParam(required = false, defaultValue = "") String category)
    {
        String[] categoryList;
        if(!category.equals(""))
            categoryList  = category.split(",");
        else
            categoryList = new String[] {"*"} ;
        return new ResponseEntity(productService.getFilteredProducts(name,barcode, qty,minPrice,maxPrice,categoryList)
                .stream().map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList()
                ), HttpStatus.OK);
    }

    /**
     * Same as searchFiltered but paged
     *
     * @param page          used to select desired page
     * @param pageSize      used to set single page dimension
     * @param sortBy        used to change sorting
     *
     * @return              List of filtered products
     *
     * @see ProductController#searchFiltered(String, String, Integer, Float, Float, String)
     * @see ProductDto
     *
     */

    @GetMapping("/advancedSearch/paged")
    public ResponseEntity searchFilteredPaged(
                                            @RequestParam(required = false, defaultValue = "") String name,
                                             @RequestParam(required = false, defaultValue = "") String barcode,
                                             @RequestParam(name = "quantity", required = false) Integer qty,
                                             @RequestParam(required = false) Float minPrice,
                                             @RequestParam(required = false) Float maxPrice,
                                             @RequestParam(required = false, defaultValue = "") String category,
                                             @RequestParam(name = "page", defaultValue = "0") Integer page,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                             @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
                                              )
    {
        String[] categoryList;
        if(!category.equals(""))
            categoryList  = category.split(",");
        else
            categoryList = new String[] {"*"} ;
        return new ResponseEntity(mapEntityPageIntoDtoPage( productService.getFilteredProducts(name,barcode, qty,minPrice,maxPrice,categoryList, page, pageSize, sortBy), ProductDto.class)
                , HttpStatus.OK);
    }

    /**
     *
     * Updates product
     *
     * @param productDto                    creates a product from a valid dto object
     *                                      given as PUT request body
     *
     * @return                              updated product
     *
     * @throws UserIdNotFoundException      if user was not found by given id
     * @throws ProductNotFoundException     if product was not found by given id
     *
     * @see ProductDto
     *
     */

    @PutMapping()
    public ResponseEntity updateProduct(@RequestBody ProductDto productDto)
    {
        try{
            Product product = modelMapper.map(productDto, Product.class);
            User user = userService.getUserByUsername(productDto.getUserUsername());
            Category category = productService.getCategory(productDto.getCategoryName());
            product.setUser(user);
            product.setCategory(category);
            return new ResponseEntity(modelMapper.map(productService.updateProduct(product), ProductDto.class), HttpStatus.OK);
        }
        catch (ProductNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_PRODUCT_NOT_FOUND",e.getMessage(),e.getId()),HttpStatus.OK);
        }
        catch (CategoryNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_CATEGORY_NOT_FOUND",e.getMessage(),e.getCategory()),HttpStatus.OK);
        } catch (UserNameNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getUserName()),HttpStatus.OK);
        }
    }


    /**
     *
     * Delete product
     *
     * @param id                            product id
     *
     * @return                              text message
     *
     * @throws ProductNotFoundException     if product was not found by given id
     *
     *
     */

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

    @GetMapping(path = "/{id}")
    public ResponseEntity getProductById(@PathVariable Long id)
    {
        try{
            return new ResponseEntity(modelMapper.map(productService.getProductById(id),ProductDto.class), HttpStatus.OK);
        }catch (ProductNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_PRODUCT_NOT_FOUND", e.getMessage(),e.getId()),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Simple method that retrieve all products
     *
     * @return List of products
     *
     * @see ProductDto
     *
     */

    @GetMapping
    public ResponseEntity getAllProducts()
    {
        return new ResponseEntity(productService.getAllProducts().stream().map(product -> modelMapper.map(product,ProductDto.class)), HttpStatus.OK);
    }

    /**
     * Same as getAllProducts but paged
     *
     * @param page                      used to select desired page
     * @param pageSize                  used to set single page dimension
     * @param sortBy                    used to change sorting
     *
     * @return List of products
     *
     * @see ProductDto
     *
     */

    @GetMapping(path = "/paged")
    public ResponseEntity getAllProductsPaged(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                              @RequestParam(name = "sortBy", defaultValue = "id") String sortBy)
    {
        return new ResponseEntity(productService.getAllProducts(page, pageSize, sortBy).stream().map(product -> modelMapper.map(product,ProductDto.class)), HttpStatus.OK);
    }

    /**
     * Retrieves products that are available( qty > 0 )
     *
     * @return List of products
     *
     * @see ProductDto
     *
     */

    @GetMapping(path = "/available")
    public ResponseEntity getAllAvailableProducts()
    {

        return new ResponseEntity(productService.getAllAvailableProducts().stream().map(product -> modelMapper.map(product,ProductDto.class)), HttpStatus.OK);
    }

    /**
     * Same as getAllAvailableProducts but paged
     *
     * @param page                      used to select desired page
     * @param pageSize                  used to set single page dimension
     * @param sortBy                    used to change sorting
     *
     * @return List of products
     *
     * @see ProductDto
     *
     */

    @GetMapping(path = "/available/paged")
    public ResponseEntity getAllAvailableProductsPaged(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                       @RequestParam(name = "sortBy", defaultValue = "id") String sortBy)
    {

        return new ResponseEntity(productService.getAllAvailableProducts(page, pageSize, sortBy).stream().map(product -> modelMapper.map(product,ProductDto.class)), HttpStatus.OK);
    }

    /**
     * Retrieve product published by user
     *
     * @param id                        user id
     *
     * @return                          List of products
     *
     * @throws UserIdNotFoundException if user was not found by given id
     *
     * @see ProductDto
     *
     */

    @GetMapping(path = "/user/{id}")
    public ResponseEntity getUserProducts(@PathVariable Long id)
    {
        try
        {
            User user = userService.getUserById(id);
            return new ResponseEntity(productService.getProductsByUser(user).stream().map(product -> modelMapper.map(product,ProductDto.class)), HttpStatus.OK);
        }catch(UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(), e.getId()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Same as getUserProducts but paged
     *
     * @param page                      used to select desired page
     * @param pageSize                  used to set single page dimension
     * @param sortBy                    used to change sorting
     *
     * @return List of products
     *
     * @throws UserIdNotFoundException if user was not found by given id
     *
     * @see ProductDto
     *
     */

    @GetMapping(path = "/user/{id}/paged")
    public ResponseEntity getUserProductsPaged(@PathVariable Long id,
                                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
                                               )
    {
        try
        {
            User user = userService.getUserById(id);
            return new ResponseEntity(mapEntityPageIntoDtoPage(productService.getProductsByUser(user,page,pageSize,sortBy), ProductDto.class), HttpStatus.OK);
        }catch(UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(), e.getId()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/categories")
    public ResponseEntity getCategories()
    {
        return new ResponseEntity(productService.getCategories(), HttpStatus.OK);
    }


    private <Dto, Entity> Page<Dto> mapEntityPageIntoDtoPage(Page<Entity> entities, Class<Dto> dtoClass) {
        return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
    }




}
