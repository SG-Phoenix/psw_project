package com.example.fakeestore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String barcode;

    private String description;
    @NotNull
    private float price;
    @NotNull
    private int quantity;
    @NotNull
    @JsonProperty(value = "user")
    private String userUsername;

    @JsonProperty(value = "category")
    private String categoryName;

    @JsonProperty(value = "brand")
    private String marca;

}
