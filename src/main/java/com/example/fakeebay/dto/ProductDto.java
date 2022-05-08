package com.example.fakeebay.dto;

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
    private Long userId;

}
