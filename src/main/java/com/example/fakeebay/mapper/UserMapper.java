package com.example.fakeebay.mapper;

import com.example.fakeebay.dto.UserDto;
import com.example.fakeebay.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserDto> {

    public UserDto convertEntityToDto(User user)
    {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEMail(user.getEMail());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public User convertDtoToEntity(UserDto dto)
    {
        User user = new User();

        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEMail(dto.getEMail());
        user.setUsername(dto.getUsername());
        return user;
    }
}
