package com.example.fakeebay.controller;

import com.example.fakeebay.dto.AddressDto;
import com.example.fakeebay.dto.UserDto;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.AddressNotFoundException;
import com.example.fakeebay.exceptions.UserNameNotFoundException;
import com.example.fakeebay.mapper.AddressMapper;
import com.example.fakeebay.mapper.UserMapper;
import com.example.fakeebay.messages.ErrorMessage;
import com.example.fakeebay.messages.ResponseMessage;
import com.example.fakeebay.exceptions.UserAllreadyExistsException;
import com.example.fakeebay.exceptions.UserIdNotFoundException;
import com.example.fakeebay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    AddressMapper addressMapper;

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserDto userDto) {
        try {

            User user = userMapper.convertDtoToEntity(userDto);
            User newUser = userService.createUser(user);
            return new ResponseEntity(userMapper.convertEntityToDto(newUser), HttpStatus.CREATED);

        } catch (UserAllreadyExistsException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_ALREADY_EXISTS",e.getMessage(),e.getUser()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity updateUser(@RequestBody UserDto userDto) {
        try {
            User user = userMapper.convertDtoToEntity(userDto);
            User updatedUser = userService.updateUser(user);
            return new ResponseEntity(userMapper.convertEntityToDto(updatedUser), HttpStatus.OK);
        } catch (UserIdNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getId()), HttpStatus.BAD_REQUEST);
        } catch (UserAllreadyExistsException e) {
        return new ResponseEntity(new ErrorMessage("ERROR_USER_ALREADY_EXISTS",e.getMessage(),e.getUser()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        return new ResponseEntity(userService.getAllUsers().stream().map(userMapper::convertEntityToDto), HttpStatus.OK);
    }

    @GetMapping(path = "by-id/{id}")
    public ResponseEntity getUserById(@PathVariable(name = "id") Long id) {
        try {

            User user = userService.getUserById(id);
            return new ResponseEntity(userMapper.convertEntityToDto(user), HttpStatus.OK);

        } catch (UserIdNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(),e.getId()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "by-username/{username}")
    public ResponseEntity getUserByUsername(@PathVariable(name = "username") String username) {
        try {
            User user = userService.getUserByUsername(username);

            return new ResponseEntity(userMapper.convertEntityToDto(user), HttpStatus.OK);

        } catch (UserNameNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getUserName()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteUser(@PathVariable(name = "id") Long id)
    {
        try
        {
            User user = userService.getUserById(id);
            userService.deleteUser(user);
            return new ResponseEntity(new ResponseMessage("USER_DELETED"), HttpStatus.OK);
        }catch (UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getId()),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(path = "/addresses")
    public ResponseEntity createAddress( @RequestBody AddressDto addressDto)
    {
        try
        {
            return new ResponseEntity(addressMapper.convertEntityToDto(userService.createAddress(addressMapper.convertDtoToEntity(addressDto))),HttpStatus.CREATED);
        }catch (UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getId()),HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(path = "/addresses")
    public ResponseEntity updateAddress( @RequestBody AddressDto addressDto)
    {
        try
        {
            return new ResponseEntity(addressMapper.convertEntityToDto(userService.updateAddress(addressMapper.convertDtoToEntity(addressDto))),HttpStatus.OK);
        }catch (AddressNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_ADDRESS_NOT_FOUND",e.getMessage(),e.getAddress()),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "{id}/addresses")
    public ResponseEntity updateAddress(@PathVariable(name = "id")Long userId)
    {
        try
        {
            User user = userService.getUserById(userId);
            return new ResponseEntity(userService.getAllUserAddresses(user).stream().map(addressMapper::convertEntityToDto).collect(Collectors.toList()), HttpStatus.OK);
        }catch (UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getId()),HttpStatus.BAD_REQUEST);
        }

    }


}
