package com.example.fakeebay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String barcode;
    private String description;
    private float price;
    private int quantity;
    @JsonProperty(value = "userId")
    private Long userId;

}
