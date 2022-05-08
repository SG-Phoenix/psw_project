package com.example.fakeebay.mapper;

import com.example.fakeebay.dto.AddressDto;
import com.example.fakeebay.entity.Address;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.UserIdNotFoundException;
import com.example.fakeebay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper implements Mapper<Address, AddressDto> {

    @Autowired
    UserService userService;

    @Override
    public AddressDto convertEntityToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setCity(address.getCity());
        dto.setCountry(address.getCountry());
        dto.setStreet(address.getStreet());
        dto.setPostalCode(address.getPostalCode());
        dto.setUserId(address.getUser().getId());
        return dto;
    }

    @Override
    public Address convertDtoToEntity(AddressDto dto) throws UserIdNotFoundException {
        User user = userService.getUserById(dto.getUserId());
        Address address = new Address();
        address.setId(dto.getId());
        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());
        address.setStreet(dto.getStreet());
        address.setPostalCode(dto.getPostalCode());
        address.setUser(user);

        return address;
    }
}
