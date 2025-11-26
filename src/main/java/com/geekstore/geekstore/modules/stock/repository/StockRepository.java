package com.geekstore.geekstore.modules.stock.repository;

import com.geekstore.geekstore.modules.stock.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.geekstore.geekstore.modules.product.model.Product;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByProductId(Long productId);
    Optional<Stock> findByProduct(Product product);
}
