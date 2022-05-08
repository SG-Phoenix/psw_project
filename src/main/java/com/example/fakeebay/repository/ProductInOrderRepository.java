package com.example.fakeebay.repository;

import com.example.fakeebay.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInOrderRepository extends JpaRepository<OrderLine, Long> {
}
