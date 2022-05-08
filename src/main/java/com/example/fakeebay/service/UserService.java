package com.example.fakeebay.service;

import com.example.fakeebay.entity.Address;
import com.example.fakeebay.entity.User;
import com.example.fakeebay.exceptions.AddressNotFoundException;
import com.example.fakeebay.exceptions.UserAllreadyExistsException;
import com.example.fakeebay.exceptions.UserNameNotFoundException;
import com.example.fakeebay.exceptions.UserIdNotFoundException;
import com.example.fakeebay.repository.AddressRepository;
import com.example.fakeebay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

