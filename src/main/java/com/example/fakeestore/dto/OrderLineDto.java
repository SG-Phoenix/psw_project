package com.example.fakeestore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineDto
{

    private Long id;
    @NotNull
    private Integer quantity;
    @NotNull
    @JsonProperty(value = "productId")
    private Long productId;
    @JsonProperty(value = "orderId")
    private Long orderId;
    @NotNull
    private Float purchasePrice;

}
