package com.example.fakeebay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private Long id;
    private Long userId;
    private String country;
    private String city;
    private String postalCode;
    private String street;

}
