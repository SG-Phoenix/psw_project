package com.example.fakeestore.service;

import com.example.fakeestore.entity.*;
import com.example.fakeestore.exceptions.*;
import com.example.fakeestore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    OrderLineRepository orderLineRepository;

    @Transactional(rollbackFor = { ProductChangedPrice.class, ProductNotEnoughtQuantity.class})
    public Order createOrder(Order order) throws ProductNotEnoughtQuantity, ProductChangedPrice {

        double totalPrice = 0;
        for(OrderLine pio : order.getProductsList())
        {
            pio.setOrder(order);
            Product product = pio.getProduct();
            if(Math.abs(product.getPrice() - pio.getPurchasePrice()) > 0.00000001)
                throw new ProductChangedPrice(product);
            pio.setProduct(product);

            int newQty = product.getQuantity() - pio.getQuantity();
            if(newQty < 0)
               throw new ProductNotEnoughtQuantity(product, pio.getQuantity());

           totalPrice += pio.getPurchasePrice()* pio.getQuantity();

           product.setQuantity(newQty);
        }

        order.setCreationDate(new Date());
        order.setTotalPrice(totalPrice);
        Order newOrder = orderRepository.save(order);
        return newOrder;
    }



    @Transactional(readOnly = true)
    public Page<Order> getAllUserOrders(User user, Date fromDate, Date toDate, int page, int pageSize, String sortBy)
    {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<Order> pagedResult = orderRepository.findByUser(user,fromDate, toDate, pageable);
        return pagedResult;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) throws OrderNotFoundException
    {
        Optional<Order> order = orderRepository.findById(id);
        if(!order.isPresent())
            throw new OrderNotFoundException(id);
        return order.get();
    }



    @Transactional(readOnly = true)
    public Page<OrderLine> getPurchasesByUser(User user, int page, int pageSize, String sortBy)
    {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<OrderLine> pagedResult = orderLineRepository.findPurchasesByUser(user, pageable);
        return pagedResult;
    }
}
