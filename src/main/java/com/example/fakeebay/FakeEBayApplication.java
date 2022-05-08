package com.example.fakeebay;

import com.example.fakeebay.controller.UserController;
import com.example.fakeebay.dto.OrderDto;
import com.example.fakeebay.dto.OrderLineDto;
import com.example.fakeebay.dto.ProductDto;
import com.example.fakeebay.dto.PurchaseDto;
import com.example.fakeebay.entity.Order;
import com.example.fakeebay.entity.OrderLine;
import com.example.fakeebay.entity.Product;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.service.ProductService;
import com.example.fakeebay.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FakeEBayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeEBayApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<OrderLine, PurchaseDto> purchaseDtoPropertyMap = new PropertyMap<OrderLine, PurchaseDto>() {
            @Override
            protected void configure() {
                map().setStreet(source.getOrder().getStreet());
                map().setCountry(source.getOrder().getCountry());
                map().setCity(source.getOrder().getCity());
                map().setPostalCode(source.getOrder().getPostalCode());
            }
        };
        modelMapper.addMappings(purchaseDtoPropertyMap);
        return modelMapper;
    }

}
