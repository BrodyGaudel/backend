package org.mounanga.backend.repository;

import org.mounanga.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.name like :keyword or p.description like :keyword")
    Page<Product> searchByNameOrDescription(@Param("keyword") String keyword, Pageable pageable);

    @Query("select p from Product p where p.barcode = :barcode")
    Optional<Product> findByBarcode(@Param("barcode") String barcode);

    boolean existsByBarcode(String barcode);
}
