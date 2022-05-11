package com.example.fakeestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String postalCode;
    @NotNull
    private String street;

}
