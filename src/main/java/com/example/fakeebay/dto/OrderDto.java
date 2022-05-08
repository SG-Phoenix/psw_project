package com.example.fakeebay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto
{

    private Long id;
    private Long userId;
    @JsonProperty(value = "products")
    private List<OrderLineDto> productsInOrder;
    @JsonProperty(value = "purchase_time")
    private Date createDate;

    private String country;
    private String city;
    private String postalCode;
    private String street;


}
