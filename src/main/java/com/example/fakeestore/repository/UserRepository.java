package com.example.fakeestore.repository;

import com.example.fakeestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Long id);
    boolean existsByEMail(String email);
    boolean existsByUsername(String username);
    @Query( "SELECT u " +
            "FROM User u " +
            "WHERE u.EMail = :EMail")
    Optional<User> findUserByEMail(@Param(value = "EMail") String eMail);
}
