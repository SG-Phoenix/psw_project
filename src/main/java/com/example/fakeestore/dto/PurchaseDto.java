package com.example.fakeestore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {

    private Long id;
    private Integer quantity;
    @JsonProperty(value = "productId")
    private Long productId;
    @JsonProperty(value = "orderId")
    private Long orderId;
    private Float purchasePrice;
    private String country;
    private String city;
    private String postalCode;
    private String street;

}