package com.example.fakeebay.mapper;

import com.example.fakeebay.dto.PurchaseDto;
import com.example.fakeebay.entity.Order;
import com.example.fakeebay.entity.OrderLine;
import org.springframework.stereotype.Component;

@Component
public class PurchaseMapper implements Mapper<OrderLine, PurchaseDto>{

    @Override
    public PurchaseDto convertEntityToDto(OrderLine orderLine) {
        PurchaseDto dto = new PurchaseDto();
        dto.setId(orderLine.getId());
        dto.setQuantity(orderLine.getQuantity());
        dto.setProductId(orderLine.getProduct().getId());
        dto.setPurchasePrice(orderLine.getPurcasePrice());

        Order order = orderLine.getOrder();
        dto.setCity(order.getCity());
        dto.setCountry(order.getCountry());
        dto.setPostalCode(order.getPostalCode());
        dto.setStreet(order.getStreet());
        dto.setOrderId(order.getId());

        return dto;
    }

    @Override
    public OrderLine convertDtoToEntity(PurchaseDto purchaseDto) {
        return null;
    }
}
