package com.example.fakeestore.service;

import com.example.fakeestore.entity.Address;
import com.example.fakeestore.entity.User;
import com.example.fakeestore.exceptions.AddressNotFoundException;
import com.example.fakeestore.exceptions.UserAllreadyExistsException;
import com.example.fakeestore.exceptions.UserNameNotFoundException;
import com.example.fakeestore.exceptions.UserIdNotFoundException;
import com.example.fakeestore.repository.AddressRepository;
import com.example.fakeestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public User createUser(User user) throws UserAllreadyExistsException
    {
        if (userRepository.existsByEMail(user.getEMail())) {
            throw new UserAllreadyExistsException(user);
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAllreadyExistsException(user);
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) throws UserIdNotFoundException
    {
        return userRepository.findUserById(id).orElseThrow(() -> {
            throw new UserIdNotFoundException(id);
        });
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) throws UserIdNotFoundException
    {
        return userRepository.findUserByUsername(username).orElseThrow(() -> {
            throw new UserNameNotFoundException(username);
        });
    }

    @Transactional
    public User updateUser(User user)  throws UserIdNotFoundException, UserAllreadyExistsException
    {
        if (!userRepository.existsById(user.getId())) {
            throw new UserIdNotFoundException(user.getId());
        }
        Optional<User> userByEMail = userRepository.findUserByEMail(user.getEMail());
        if(userByEMail.isPresent() && !(userByEMail.get().getId().equals(user.getId())))
        {
            throw new UserAllreadyExistsException(user);
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers(int page, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<User> pagedResult = userRepository.findAll(pageable);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public void deleteUser(User user) {
            userRepository.delete(user);
    }

    @Transactional
    public Address createAddress(Address address) throws UserIdNotFoundException
    {   if(!userRepository.existsById(address.getUser().getId()))
            throw new UserIdNotFoundException(address.getUser().getId());
        return addressRepository.save(address);
    }

    @Transactional
    public Address updateAddress(Address address) throws AddressNotFoundException
    {
        if (!addressRepository.existsById(address.getId())) {
            throw new AddressNotFoundException(address);
        }

        return addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public List<Address> getAllUserAddresses(User user) throws UserIdNotFoundException
    {   if(!userRepository.existsById(user.getId()))
        throw new UserIdNotFoundException(user.getId());
        return addressRepository.findAddressesByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Address> getAllUserAddresses(User user, int page, int pageSize, String sortBy) throws UserIdNotFoundException
    {
        Pageable pageable = PageRequest.of(page,pageSize, Sort.by(sortBy));
        Page<Address> pagedResult = addressRepository.findAddressesByUser(user, pageable);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }
}

