package com.example.fakeestore.service;

import com.example.fakeestore.entity.*;
import com.example.fakeestore.exceptions.OrderNotFoundException;
import com.example.fakeestore.exceptions.ProductNotEnoughtQuantity;
import com.example.fakeestore.repository.OrderLineRepository;
import com.example.fakeestore.repository.OrderRepository;
import com.example.fakeestore.repository.ProductInOrderRepository;
import com.example.fakeestore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    OrderLineRepository orderLineRepository;

    @Transactional
    public Order createOrder(Order order) throws ProductNotEnoughtQuantity {
        Order newOrder = orderRepository.save(order);
        for(OrderLine pio : newOrder.getProductsList())
        {
            pio.setOrder(newOrder);
            OrderLine newPio = productInOrderRepository.save(pio);
            Product product = newPio.getProduct();
           int newQty = product.getQuantity() - newPio.getQuantity();
           if(newQty < 0)
               throw new ProductNotEnoughtQuantity(product, newPio.getQuantity());
           product.setQuantity(newQty);
        }

        return newOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders()
    {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders(int page, int pageSize, String sortBy)
    {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<Order> pagedResult = orderRepository.findAll(pageable);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }


    @Transactional(readOnly = true)
    public List<Order> getAllUserOrders(User user, Date fromDate, Date toDate)
    {
        return orderRepository.findByUser(user, fromDate, toDate);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllUserOrders(User user, Date fromDate, Date toDate, int page, int pageSize, String sortBy)
    {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<Order> pagedResult = orderRepository.findByUser(user,fromDate, toDate, pageable);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) throws OrderNotFoundException
    {
        return orderRepository.findById(id).orElseThrow(() -> {throw new OrderNotFoundException(id);});
    }



    @Transactional(readOnly = true)
    public List<OrderLine> getPurchasesByProduct(Product product) throws OrderNotFoundException
    {
        return orderLineRepository.findPurchasesByProduct(product);
    }

    @Transactional(readOnly = true)
    public List<OrderLine> getPurchasesByProduct(Product product, int page, int pageSize, String sortBy) throws OrderNotFoundException
    {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<OrderLine> pagedResult = orderLineRepository.findPurchasesByProduct(product, pageable);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderLine> getPurchasesByUser(User user)
    {
        return orderLineRepository.findPurchasesByUser(user);
    }

    @Transactional(readOnly = true)
    public List<OrderLine> getPurchasesByUser(User user, int page, int pageSize, String sortBy)
    {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<OrderLine> pagedResult = orderLineRepository.findPurchasesByUser(user, pageable);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }
}
