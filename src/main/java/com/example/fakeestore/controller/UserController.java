package com.example.fakeestore.controller;

import com.example.fakeestore.dto.AddressDto;
import com.example.fakeestore.dto.OrderDto;
import com.example.fakeestore.dto.ProductDto;
import com.example.fakeestore.dto.UserDto;
import com.example.fakeestore.entity.Address;
import com.example.fakeestore.entity.User;
import com.example.fakeestore.exceptions.AddressNotFoundException;
import com.example.fakeestore.exceptions.UserNameNotFoundException;
import com.example.fakeestore.messages.ErrorMessage;
import com.example.fakeestore.messages.ResponseMessage;
import com.example.fakeestore.exceptions.UserAllreadyExistsException;
import com.example.fakeestore.exceptions.UserIdNotFoundException;
import com.example.fakeestore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private ModelMapper modelMapper;

    /**
     *
     * Creates new user
     *
     * @param userDto                    creates a user from a valid dto object
     *                                   given as POST request body
     *
     * @return                              new product
     *
     * @throws UserIdNotFoundException      if user was not found by given id
     * @throws UserAllreadyExistsException  if some unique constraints are not respected
     *
     * @see UserDto
     *
     */

    @PostMapping
    public ResponseEntity createUser(@Valid @RequestBody UserDto userDto) {
        try {

            User user = modelMapper.map(userDto, User.class);
            return new ResponseEntity(modelMapper.map(userService.createUser(user), UserDto.class), HttpStatus.CREATED);

        } catch (UserAllreadyExistsException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_ALREADY_EXISTS",e.getMessage(),e.getUser()), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     *
     * Updates user
     *
     * @param userDto                       creates a user object from a valid dto object
     *                                      given as PUT request body
     *
     * @return                              updated product
     *
     * @throws UserIdNotFoundException      if user was not found by given id
     * @throws UserAllreadyExistsException  if some unique constraints are not respected
     *
     * @see UserDto
     *
     */

    @PutMapping
    public ResponseEntity updateUser(@Valid @RequestBody UserDto userDto) {
        try {
            User user = modelMapper.map(userDto, User.class);
            return new ResponseEntity(modelMapper.map(userService.updateUser(user), UserDto.class), HttpStatus.OK);
        } catch (UserIdNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getId()), HttpStatus.BAD_REQUEST);
        } catch (UserAllreadyExistsException e) {
        return new ResponseEntity(new ErrorMessage("ERROR_USER_ALREADY_EXISTS",e.getMessage(),e.getUser()), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Simple method that retrieve all users
     *
     * @return List of users
     *
     * @see UserDto
     *
     */

    @GetMapping
    public ResponseEntity getAllUsers() {
        return new ResponseEntity(userService.getAllUsers().stream().map(user -> modelMapper.map(user, UserDto.class)), HttpStatus.OK);
    }

    /**
     * Same use of getAllUsers but paged
     *
     * @param page                      used to select desired page
     * @param pageSize                  used to set single page dimension
     * @param sortBy                    used to change sorting
     *
     * @return                          List of users
     *
     * @see UserDto
     *
     */

    @GetMapping(path = "/paged")
    public ResponseEntity getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy
    ) {
        return new ResponseEntity(userService.getAllUsers(page, pageSize, sortBy).stream().map(user -> modelMapper.map(user, UserDto.class)), HttpStatus.OK);
    }

    /**
     * Simple method that retrieve particular user by id
     *
     * @param id                            User id
     * @return                              User object
     *
     * @see UserDto
     * @throws UserIdNotFoundException      if user was not found by given id
     *
     */

    @GetMapping(path = "by-id/{id}")
    public ResponseEntity getUserById(@PathVariable(name = "id") Long id) {
        try {

            User user = userService.getUserById(id);
            return new ResponseEntity(modelMapper.map(user, UserDto.class), HttpStatus.OK);

        } catch (UserIdNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND", e.getMessage(),e.getId()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Simple method that retrieve particular user by id
     *
     * @param username                      User username
     * @return                              User object
     *
     * @see UserDto
     * @throws UserNameNotFoundException    if user was not found by given username
     *
     */

    @GetMapping(path = "by-username/{username}")
    public ResponseEntity getUserByUsername(@PathVariable(name = "username") String username) {
        try {
            User user = userService.getUserByUsername(username);

            return new ResponseEntity(modelMapper.map(user, UserDto.class), HttpStatus.OK);

        } catch (UserNameNotFoundException e) {
            return new ResponseEntity(new ErrorMessage("ERROR_USER_NOT_FOUND",e.getMessage(),e.getUserName()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes user
     *
     * @param id                            User id
     * @return                              Text message
     *
     * @see UserDto
     * @throws UserIdNotFoundException    if user was not found by given id
     *
     */

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

    /**
     *
     * Creates new address
     *
     * @param addressDto                    creates address from a valid dto object
     *                                      given as POST request body
     * @return                              new address
     *
     * @throws UserIdNotFoundException      if user was not found by given id
     *
     * @see AddressDto
     *
     */

    @PostMapping(path = "/addresses")
    public ResponseEntity createAddress(@Valid @RequestBody AddressDto addressDto)
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

    /**
     *
     * Updates address
     *
     * @param addressDto                    creates address from a valid dto object
     *                                      given as PUT request body
     * @return                              updated address
     *
     * @throws AddressNotFoundException     if address was not found by given id
     *
     * @see AddressDto
     *
     */
    @PutMapping(path = "/addresses")
    public ResponseEntity updateAddress(@Valid @RequestBody AddressDto addressDto)
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

    /**
     * Simple method that retrieve all user addresses
     *
     * @param userId                        user id
     * @return                              List of addresses
     *
     * @throws UserIdNotFoundException      if user was not found by given id
     * @see AddressDto
     *
     */

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

    /**
     * Same use of getAddresses but paged
     *
     * @param page                      used to select desired page
     * @param pageSize                  used to set single page dimension
     * @param sortBy                    used to change sorting
     *
     * @return                          List of addresses
     *
     * @see AddressDto
     * @see UserController#getAddresses(Long) 
     *
     */

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
