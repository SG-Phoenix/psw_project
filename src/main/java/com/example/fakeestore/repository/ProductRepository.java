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

    @Query("Select p from Product p where p.name like %:text% or p.barcode like %:text% ")
    List<Product> findProductByNameContainingOrBarcodeContaining(@Param(value = "text") String text);

    @Query("Select p from Product p where p.name like %:text% or p.barcode like %:text% ")
    Page<Product> findProductByNameContainingOrBarcodeContaining(@Param(value = "text") String text, Pageable pageable);
    List<Product> findProductByUser(User user);
    Page<Product> findProductByUser(User user, Pageable pageable);

    @Query("Select p from Product p where p.quantity > 0")
    List<Product> findAllAvailableProducts();

    @Query("Select p from Product p where p.quantity > 0")
    Page<Product> findAllAvailableProducts(Pageable pageable);

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE (p.name LIKE %:name%) AND" +
            "(p.barcode LIKE %:barcode%) AND" +
            "(p.category.name in :category OR '*' in :category) AND" +
            "(p.quantity > :qty OR :qty IS NULL) AND" +
            "(p.price >= :minPrice OR :minPrice IS NULL) AND" +
            "(p.price < :maxPrice OR :maxPrice IS NULL)")
    List<Product> advancedFilter(@Param(value = "name") String name,
                                 @Param(value = "barcode") String barcode,
                                 @Param(value = "qty") Integer qty,
                                 @Param(value = "minPrice") Float minPrice,
                                 @Param(value = "maxPrice") Float maxPrice,
                                 @Param(value = "category") String category[]
                                 );

    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE (p.name LIKE %:name%) AND" +
            "(p.barcode LIKE %:barcode%) AND" +
            "(p.category.name in :category OR '*' in :category) AND" +
            "(p.quantity > :qty OR :qty IS NULL) AND" +
            "(p.price >= :minPrice OR :minPrice IS NULL) AND" +
            "(p.price < :maxPrice OR :maxPrice IS NULL)")
    Page<Product> advancedFilter(@Param(value = "name") String name,
                                 @Param(value = "barcode") String barcode,
                                 @Param(value = "qty") Integer qty,
                                 @Param(value = "minPrice") Float minPrice,
                                 @Param(value = "maxPrice") Float maxPrice,
                                 @Param(value = "category") String category[],
                                 Pageable pageable);

    Page<Product> findByOrderByIdDesc(Pageable pageable);
    @Query("SELECT p " +
            "FROM Product p " +
            "WHERE (p.id in :idList)")
    Page<Product> getRandomProducts(@Param(value = "idList")Long[] randId, Pageable pageable);

}
