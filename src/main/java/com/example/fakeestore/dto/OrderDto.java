package com.example.fakeestore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto
{

    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    @JsonProperty(value = "products")
    private List<OrderLineDto> productsList;

    @JsonProperty(value = "purchase_time")
    private Date createDate;

    @NotNull
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String postalCode;
    @NotNull
    private String street;


}
