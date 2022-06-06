package com.example.fakeestore.repository;

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
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p " +
            "from Product p " +
            "Where p.user=:user AND p.isAvailable = TRUE")
    Page<Product> findProductByUser(User user, Pageable pageable);


    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE (p.name LIKE %:name%) AND" +
            "(p.barcode LIKE %:barcode%) AND" +
            "(p.category.name in :category OR '*' in :category) AND" +
            "(p.quantity > :qty OR :qty IS NULL) AND" +
            "(p.price >= :minPrice OR :minPrice IS NULL) AND" +
            "(p.price < :maxPrice OR :maxPrice IS NULL) AND " +
            "p.isAvailable = TRUE ")
    Page<Product> advancedFilter(@Param(value = "name") String name,
                                 @Param(value = "barcode") String barcode,
                                 @Param(value = "qty") Integer qty,
                                 @Param(value = "minPrice") Float minPrice,
                                 @Param(value = "maxPrice") Float maxPrice,
                                 @Param(value = "category") String category[],
                                 Pageable pageable);


    @Query("select p " +
            "From Product p " +
            "Where p.isAvailable = TRUE " +
            "order by p.id desc")
    Page<Product> findByOrderByIdDesc(Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE (p.id in :idList)" +
            "AND p.isAvailable = TRUE")
    Page<Product> getRandomProducts(@Param(value = "idList")long[] randId, Pageable pageable);

}
