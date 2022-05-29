package com.example.fakeestore;

import com.example.fakeestore.dto.PurchaseDto;
import com.example.fakeestore.entity.OrderLine;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

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
                map().setOrderDate(source.getOrder().getCreationDate());
            }
        };
        modelMapper.addMappings(purchaseDtoPropertyMap);
        return modelMapper;
    }



}
