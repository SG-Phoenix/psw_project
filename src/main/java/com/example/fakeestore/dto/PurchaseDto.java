package com.example.fakeestore.dto;

import com.example.fakeestore.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {

    private Long id;
    private Integer quantity;
    @JsonProperty(value = "product")
    private Product product;
    @JsonProperty(value = "orderId")
    private Long orderId;
    private Float purchasePrice;
    private String country;
    private String city;
    private String postalCode;
    private String street;
    private Date orderDate;

}
