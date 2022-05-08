package com.example.fakeebay.repository;

import com.example.fakeebay.entity.Product;
import com.example.fakeebay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("Select p from Product p where p.name like %:text% or p.barcode like %:text% ")
    List<Product> findProductByNameContainingOrBarcodeContaining(@Param(value = "text") String text);
    List<Product> findProductByUser(User user);

    @Query("Select p from Product p where p.quantity > 0")
    List<Product> findAllAvailableProducts();

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE (p.name LIKE %:name%) AND" +
            "(p.barcode LIKE %:barcode%) AND" +
            "(p.quantity > :qty OR :qty IS NULL) AND" +
            "(p.price >= :minPrice OR :minPrice IS NULL) AND" +
            "(p.price < :maxPrice OR :maxPrice IS NULL)")
    List<Product> advancedFilter(@Param(value = "name") String name,
                                 @Param(value = "barcode") String barcode,
                                 @Param(value = "qty") Integer qty,
                                 @Param(value = "minPrice") Float minPrice,
                                 @Param(value = "maxPrice") Float maxPrice);

}
