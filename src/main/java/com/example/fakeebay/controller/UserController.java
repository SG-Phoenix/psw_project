package com.example.fakeebay.controller;

import com.example.fakeebay.dto.AddressDto;
import com.example.fakeebay.dto.UserDto;
import com.example.fakeebay.entity.Address;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.AddressNotFoundException;
import com.example.fakeebay.exceptions.UserNameNotFoundException;
import com.example.fakeebay.messages.ErrorMessage;
import com.example.fakeebay.messages.ResponseMessage;
import com.example.fakeebay.exceptions.UserAllreadyExistsException;
import com.example.fakeebay.exceptions.UserIdNotFoundException;
import com.example.fakeebay.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    public ResponseEntity createUser(@RequestBody UserDto userDto) {
        try {

            User user = modelMapper.map(userDto, User.class);
            return new ResponseEntity(modelMapper.map(userService.createUser(user), UserDto.class), HttpStatus.CREATED);

        } catch (UserAllreadyExistsException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_ALREADY_EXISTS",e.getMessage(),e.getUser()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity updateUser(@RequestBody UserDto userDto) {
        try {
            User user = modelMapper.map(userDto, User.class);
            return new ResponseEntity(modelMapper.map(userService.updateUser(user), UserDto.class), HttpStatus.OK);
        } catch (UserIdNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getId()), HttpStatus.BAD_REQUEST);
        } catch (UserAllreadyExistsException e) {
        return new ResponseEntity(new ErrorMessage("ERROR_USER_ALREADY_EXISTS",e.getMessage(),e.getUser()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity getAllUsers() {
        return new ResponseEntity(userService.getAllUsers().stream().map(user -> modelMapper.map(user, UserDto.class)), HttpStatus.OK);
    }

    @GetMapping(path = "/paged")
    public ResponseEntity getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
    ) {
        return new ResponseEntity(userService.getAllUsers(page, pageSize, sortBy).stream().map(user -> modelMapper.map(user, UserDto.class)), HttpStatus.OK);
    }

    @GetMapping(path = "by-id/{id}")
    public ResponseEntity getUserById(@PathVariable(name = "id") Long id) {
        try {

            User user = userService.getUserById(id);
            return new ResponseEntity(modelMapper.map(user, UserDto.class), HttpStatus.OK);

        } catch (UserIdNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(),e.getId()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "by-username/{username}")
    public ResponseEntity getUserByUsername(@PathVariable(name = "username") String username) {
        try {
            User user = userService.getUserByUsername(username);

            return new ResponseEntity(modelMapper.map(user, UserDto.class), HttpStatus.OK);

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
            User user = userService.getUserById(addressDto.getUserId());
            Address address = modelMapper.map(addressDto,Address.class);
            address.setUser(user);
            return new ResponseEntity(modelMapper.map(userService.createAddress(address), AddressDto.class),HttpStatus.CREATED);
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
            Address address = modelMapper.map(addressDto,Address.class);
            return new ResponseEntity(modelMapper.map(userService.updateAddress(address), AddressDto.class),HttpStatus.OK);
        }catch (AddressNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_ADDRESS_NOT_FOUND",e.getMessage(),e.getAddress()),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "{id}/addresses")
    public ResponseEntity getAddresses(@PathVariable(name = "id")Long userId)
    {
        try
        {
            User user = userService.getUserById(userId);
            return new ResponseEntity(userService.getAllUserAddresses(user).stream().map(address -> modelMapper.map(address, AddressDto.class)).collect(Collectors.toList()), HttpStatus.OK);
        }catch (UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getId()),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(path = "{id}/addresses/paged")
    public ResponseEntity getAddressesPaged(@PathVariable(name = "id")Long userId,
                                            @RequestParam(name = "page", defaultValue = "0") Integer page,
                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
                                            )
    {
        try
        {
            User user = userService.getUserById(userId);
            return new ResponseEntity(userService.getAllUserAddresses(user, page, pageSize, sortBy).stream().map(address -> modelMapper.map(address, AddressDto.class)).collect(Collectors.toList()), HttpStatus.OK);
        }catch (UserIdNotFoundException e)
        {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getId()),HttpStatus.BAD_REQUEST);
        }

    }


}
