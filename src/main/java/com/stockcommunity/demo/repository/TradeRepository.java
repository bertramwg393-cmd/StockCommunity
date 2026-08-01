package com.stockcommunity.demo.repository;

import com.stockcommunity.demo.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    // 查某筆買單相關的所有成交紀錄
    List<Trade> findByBuyOrderId(Long buyOrderId);

    // 查某筆賣單相關的所有成交紀錄
    List<Trade> findBySellOrderId(Long sellOrderId);

    // 查某檔股票的所有成交紀錄（之後可能用在「查看某股票近期成交」這類功能）
    List<Trade> findByStockCode(String stockCode);
}
