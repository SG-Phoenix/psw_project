package com.example.fakeebay.mapper;

import com.example.fakeebay.dto.ProductDto;
import com.example.fakeebay.entity.Product;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.UserIdNotFoundException;
import com.example.fakeebay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements Mapper<Product, ProductDto> {

    @Autowired
    UserService userService;

    public ProductDto convertEntityToDto(Product product)
    {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setBarcode(product.getBarcode());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setUserId(product.getUser().getId());
        return dto;
    }

    public Product convertDtoToEntity(ProductDto dto)
    {
        User user = null;
        try
        {
           user = userService.getUserById(dto.getUserId());
        }catch (UserIdNotFoundException e)
        {
            throw e;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setBarcode(dto.getBarcode());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setUser(user);
        return product;
    }
}
