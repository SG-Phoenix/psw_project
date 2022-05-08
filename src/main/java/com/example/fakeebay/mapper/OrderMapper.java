package com.example.fakeebay.mapper;

import com.example.fakeebay.dto.OrderDto;
import com.example.fakeebay.dto.OrderLineDto;
import com.example.fakeebay.entity.Order;
import com.example.fakeebay.entity.OrderLine;
import com.example.fakeebay.entity.Product;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.ProductNotFoundException;
import com.example.fakeebay.exceptions.UserIdNotFoundException;
import com.example.fakeebay.service.ProductService;
import com.example.fakeebay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class OrderMapper implements Mapper<Order, OrderDto>
{

    @Autowired
    OrderLineMapper orderLineMapper;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;


    @Override
    public OrderDto convertEntityToDto(Order order)
    {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setCreateDate(order.getCreateDate());
        dto.setCity(order.getCity());
        dto.setCountry(order.getCountry());
        dto.setPostalCode(order.getPostalCode());
        dto.setStreet(order.getStreet());
        dto.setProductsInOrder(order.getProductsList().stream().map(orderLineMapper::convertEntityToDto).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public Order convertDtoToEntity(OrderDto dto) {
        User user = null;
        try {
            user = userService.getUserById(dto.getUserId());
        } catch (UserIdNotFoundException e) {
            throw e;
        }
        Order order = new Order();
        order.setProductsList(new ArrayList<>());
        order.setUser(user);
        order.setStreet(dto.getStreet());
        order.setCity(dto.getCity());
        order.setCountry(dto.getCountry());
        order.setPostalCode(dto.getPostalCode());
        for(OrderLineDto old : dto.getProductsInOrder())
        {
            try{
                OrderLine ol = orderLineMapper.convertDtoToEntity(old);
                Product product = productService.getProductById(old.getProductId());

                ol.setProduct(product);
                ol.setOrder(order);

                order.getProductsList().add(ol);

            }catch(ProductNotFoundException e)
            {
                throw e;
            }

        }

        return order;
    }
}
