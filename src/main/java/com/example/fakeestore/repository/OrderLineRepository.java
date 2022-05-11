package com.example.fakeestore.repository;

import com.example.fakeestore.entity.OrderLine;
import com.example.fakeestore.entity.Product;
import com.example.fakeestore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    List<OrderLine> findPurchasesByProduct(Product product);
    Page<OrderLine> findPurchasesByProduct(Product product, Pageable pageable);
    @Query("select ol from OrderLine ol where ol.product.user=:user")
    List<OrderLine> findPurchasesByUser(@Param(value = "user") User user);

    @Query("select ol from OrderLine ol where ol.product.user=:user")
    Page<OrderLine> findPurchasesByUser(@Param(value = "user") User user, Pageable pageable);
}
