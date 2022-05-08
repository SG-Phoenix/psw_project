package com.example.fakeebay.controller;

import com.example.fakeebay.dto.OrderDto;
import com.example.fakeebay.entity.Order;
import com.example.fakeebay.entity.Product;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.OrderNotFoundException;
import com.example.fakeebay.exceptions.ProductNotEnoughtQuantity;
import com.example.fakeebay.exceptions.ProductNotFoundException;
import com.example.fakeebay.exceptions.UserIdNotFoundException;
import com.example.fakeebay.mapper.OrderLineMapper;
import com.example.fakeebay.mapper.OrderMapper;
import com.example.fakeebay.mapper.PurchaseMapper;
import com.example.fakeebay.messages.ErrorMessage;
import com.example.fakeebay.service.OrderService;
import com.example.fakeebay.service.ProductService;
import com.example.fakeebay.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;


    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private ProductService productService;


    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderDto orderDto)
    {
        try
        {
            Order order = orderMapper.convertDtoToEntity(orderDto);

            return new ResponseEntity(orderService.createOrder(order), HttpStatus.CREATED);
        }catch(ProductNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_PRODUCT_NOT_FOUND", e.getMessage(),e.getId()), HttpStatus.BAD_REQUEST);
        }
        catch (UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(), e.getId()), HttpStatus.BAD_REQUEST);
        }
        catch (ProductNotEnoughtQuantity e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_PRODUCT_NOT_ENOUGHT", e.getMessage(), e.getProduct()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "user/{id}")
    public ResponseEntity getOrdersByUser(@PathVariable Long id,
                                          @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                          @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate)
    {
        try
        {
            User user = userService.getUserById(id);
            return new ResponseEntity(orderService.getAllUserOrders(user, fromDate, toDate), HttpStatus.OK);
        }catch(UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(), e.getId()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity getAllOrders()
    {
        return new ResponseEntity(orderService.getAllOrders().stream().map(post -> modelMapper.map(post, OrderDto.class))
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getOrderById(@PathVariable Long id)
    {
        try
        {
            return new ResponseEntity(orderService.getOrderById(id),HttpStatus.OK);
        }catch(OrderNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_ORDER_NOT_FOUND",e.getMessage(),e.getId()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "/purchases/by-product/{id}")
    public ResponseEntity getProductPurchases(@PathVariable Long id)
    {
        try
        {
            Product product = productService.getProductById(id);

            return new ResponseEntity(orderService.getPurchasesByProduct(product).stream().map(purchaseMapper::convertEntityToDto).collect(Collectors.toList()),HttpStatus.OK);

        }catch(ProductNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_PRODUCT_NOT_FOUND", e.getMessage(), e.getId()), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "/purchases/by-user/{id}")
    public ResponseEntity getUserPurchases(@PathVariable Long id)
    {
        try
        {
            User user = userService.getUserById(id);
            return new ResponseEntity(orderService.getPurchasesByUser(user).stream().map(purchaseMapper::convertEntityToDto).collect(Collectors.toList()), HttpStatus.OK);

        }catch(UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(), e.getId()), HttpStatus.BAD_REQUEST);
        }

    }



}
