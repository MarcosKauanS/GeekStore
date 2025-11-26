package com.geekstore.geekstore.modules.product.repository;

import com.geekstore.geekstore.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Busca por nome contendo o termo (ILIKE)
    List<Product> findByNameContainingIgnoreCase(String name);
}