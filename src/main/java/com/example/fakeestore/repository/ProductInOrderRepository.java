package com.example.fakeestore.repository;

import com.example.fakeestore.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInOrderRepository extends JpaRepository<OrderLine, Long> {
}
