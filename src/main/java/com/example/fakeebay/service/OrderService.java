package com.example.fakeebay.service;

import com.example.fakeebay.entity.Order;
import com.example.fakeebay.entity.Product;
import com.example.fakeebay.entity.OrderLine;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.OrderNotFoundException;
import com.example.fakeebay.exceptions.ProductNotEnoughtQuantity;
import com.example.fakeebay.repository.OrderLineRepository;
import com.example.fakeebay.repository.OrderRepository;
import com.example.fakeebay.repository.ProductInOrderRepository;
import com.example.fakeebay.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
    public List<Order> getAllUserOrders(User user, Date fromDate, Date toDate)
    {
        return orderRepository.findByUser(user, fromDate, toDate);
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
    public List<OrderLine> getPurchasesByUser(User user)
    {
        return orderLineRepository.findPurchasesByUser(user);
    }
}
