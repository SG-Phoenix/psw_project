package com.example.fakeebay.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineDto
{

    private Long id;
    private Integer quantity;
    @JsonProperty(value = "productId")
    private Long productId;
    @JsonProperty(value = "orderId")
    private Long orderId;
    private Float purchasePrice;

}
