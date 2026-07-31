package com.stockcommunity.demo.repository;

import com.stockcommunity.demo.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, String> {
}
