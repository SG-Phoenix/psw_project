package com.example.fakeebay.repository;

import com.example.fakeebay.entity.Address;
import com.example.fakeebay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAddressesByUser(User user);

    Page<Address> findAddressesByUser(User user, Pageable pageable);
}
