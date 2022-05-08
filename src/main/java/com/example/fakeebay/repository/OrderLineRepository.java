package com.example.fakeebay.repository;

import com.example.fakeebay.entity.OrderLine;
import com.example.fakeebay.entity.Product;
import com.example.fakeebay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    List<OrderLine> findPurchasesByProduct(Product product);

    @Query("select ol from OrderLine ol where ol.product.user=:user")
    List<OrderLine> findPurchasesByUser(@Param(value = "user") User user);
}
