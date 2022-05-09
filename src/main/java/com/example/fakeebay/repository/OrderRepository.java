package com.example.fakeebay.repository;

import com.example.fakeebay.entity.Order;
import com.example.fakeebay.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.user=:user AND (o.createDate >= :fromDate OR :fromDate IS NULL) AND (o.createDate <= :toDate OR :toDate IS NULL)" +
            "ORDER BY o.id DESC ")
    List<Order> findByUser(User user, Date fromDate, Date toDate);

    @Query("SELECT o " +
          "FROM Order o " +
          "WHERE o.user=:user AND (o.createDate >= :fromDate OR :fromDate IS NULL) AND (o.createDate <= :toDate OR :toDate IS NULL)" +
          "ORDER BY o.id DESC ")
    Page<Order> findByUser(User user, Date fromDate, Date toDate, Pageable pageable);

}
