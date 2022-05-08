package com.example.fakeebay.mapper;

import com.example.fakeebay.dto.OrderLineDto;
import com.example.fakeebay.entity.Order;
import com.example.fakeebay.entity.OrderLine;
import com.example.fakeebay.entity.Product;
import com.example.fakeebay.exceptions.ProductNotFoundException;
import com.example.fakeebay.service.ProductService;
import org.hibernate.annotations.Cascade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderLineMapper implements Mapper<OrderLine, OrderLineDto>{

    @Autowired
    ProductService productService;
    @Override
    public OrderLineDto convertEntityToDto(OrderLine orderLine) {
        OrderLineDto dto = new OrderLineDto();
        dto.setId(orderLine.getId());
        dto.setOrderId(orderLine.getOrder().getId());
        dto.setProductId(orderLine.getProduct().getId());
        dto.setQuantity(orderLine.getQuantity());
        dto.setPurchasePrice(orderLine.getPurcasePrice());
        return dto;
    }

    @Override
    public OrderLine convertDtoToEntity(OrderLineDto orderLineDto) {
        Product product = null;
        try{
            product = productService.getProductById(orderLineDto.getProductId());
        }catch(ProductNotFoundException e)
        {
            throw e;
        }
        OrderLine orderLine = new OrderLine();
        orderLine.setId(orderLineDto.getId());
        orderLine.setQuantity(orderLineDto.getQuantity());
        orderLine.setProduct(product);
        orderLine.setPurcasePrice(product.getPrice());
        return orderLine;
    }
}
