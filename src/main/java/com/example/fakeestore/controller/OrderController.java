package com.example.fakeestore.controller;

import com.example.fakeestore.dto.OrderDto;
import com.example.fakeestore.dto.OrderLineDto;
import com.example.fakeestore.dto.PurchaseDto;
import com.example.fakeestore.entity.Order;
import com.example.fakeestore.entity.OrderLine;
import com.example.fakeestore.entity.Product;
import com.example.fakeestore.entity.User;
import com.example.fakeestore.exceptions.*;
import com.example.fakeestore.messages.ErrorMessage;
import com.example.fakeestore.service.OrderService;
import com.example.fakeestore.service.ProductService;
import com.example.fakeestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     *
     *                                      Creates new order
     *
     * @param orderDto                      creates an order from a valid dto object
     *                                      given as POST request body
     *
     * @return                              new order
     *
     * @throws ProductNotFoundException     if any product in OrderDto was not found
     * @throws UserIdNotFoundException      if user was not found by given id in OrderDto
     * @throws ProductNotEnoughtQuantity    if any product has no sufficient quantity
     *
     * @see OrderDto
     */

    @PostMapping
    public ResponseEntity createOrder(@RequestBody OrderDto orderDto)
    {
        try
        {
            Order order = modelMapper.map(orderDto, Order.class);

            User user = userService.getUserById(orderDto.getUserId());
            List<OrderLine> orderLineList = new ArrayList<>();
            for(OrderLineDto old : orderDto.getProductsList())
            {
                OrderLine ol = modelMapper.map(old, OrderLine.class);
                Product product = productService.getProductById(old.getProductId());
                ol.setProduct(product);
                ol.setOrder(order);
                ol.setPurchasePrice(old.getPurchasePrice());

                orderLineList.add(ol);


            }
            order.setUser(user);
            user.getOrders().add(order);
            order.setProductsList(orderLineList);
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
        } catch (ProductChangedPrice e) {
            return new ResponseEntity(new ErrorMessage("ERROR_PRODUCT_CHANGED_PRICE", e.getMessage(), e.getProduct()), HttpStatus.BAD_REQUEST);
        }

    }


    /**
     * Same use of getOrdersByUser but paged
     *
     * @param page                      used to select desired page
     * @param pageSize                  used to set single page dimension
     * @param sortBy                    used to change sorting
     *
     * @return                          List of orders
     *
     * @throws UserIdNotFoundException  if user was not found by given id
     *
     * @see OrderDto
     */

    @GetMapping(path = "user/{id}/paged")
    public ResponseEntity getOrdersByUserPaged(@PathVariable Long id,
                                          @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                          @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                          @RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
                                            )
    {
        try
        {
            User user = userService.getUserById(id);
            return new ResponseEntity(orderService.getAllUserOrders(user, fromDate, toDate, page, pageSize, sortBy), HttpStatus.OK);
        }catch(UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(), e.getId()), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Retrieves a particular order by his id
     *
     * @param id                        Order id
     *
     * @return                          OrderDto object
     *
     * @throws OrderNotFoundException   if there is no order with given id
     * @see OrderDto
     *
     */
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


    /**
     * Same as getUserPurchases
     *
     * @param page                      used to select desired page
     * @param pageSize                  used to set single page dimension
     * @param sortBy                    used to change sorting
     *
     * @return                          List of PurchaseDto objects
     *
     * @throws UserIdNotFoundException  if there is no user with given id
     *
     * @see PurchaseDto
     */

    @GetMapping(path = "/purchases/by-user/{id}/paged")
    public ResponseEntity getUserPurchasesPaged(@PathVariable Long id,
                                                @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                @RequestParam(name = "sortBy", defaultValue = "id") String sortBy)
    {
        try
        {
            User user = userService.getUserById(id);
            return new ResponseEntity(mapEntityPageIntoDtoPage(orderService.getPurchasesByUser(user,page, pageSize, sortBy), PurchaseDto.class), HttpStatus.OK);

        }catch(UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(), e.getId()), HttpStatus.BAD_REQUEST);
        }

    }

    private <Dto, Entity> Page<Dto> mapEntityPageIntoDtoPage(Page<Entity> entities, Class<Dto> dtoClass) {
        return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
    }



}
