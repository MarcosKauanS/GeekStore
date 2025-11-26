package com.geekstore.geekstore.modules.stock.service;

import com.geekstore.geekstore.modules.stock.model.Stock;
import com.geekstore.geekstore.modules.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import com.geekstore.geekstore.modules.product.model.Product;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Optional<Stock> findById(Long id) {
        return stockRepository.findById(id);
    }

    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    public void deleteById(Long id) {
        stockRepository.deleteById(id);
    }

    public Optional<Stock> findByProduct(Product product) {
        return stockRepository.findByProduct(product);
    }
}
