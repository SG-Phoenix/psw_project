package com.example.fakeebay;

import com.example.fakeebay.dto.ProductDto;
import com.example.fakeebay.entity.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FakeEBayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeEBayApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        /*PropertyMap<Product, ProductDto> productMap = new PropertyMap<Product, ProductDto>() {
            protected void configure() {
                map().setUser();s.getSons()...//here is your choice of coding
                // you can either use streams or simple for loops to transform the
                // entity into a List<Long>
         );
                //other attributes here
            }
        };*/
        return modelMapper;
    }

}
