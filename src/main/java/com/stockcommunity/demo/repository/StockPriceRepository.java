package com.stockcommunity.demo.repository;

import com.stockcommunity.demo.entity.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {

    // 查某檔股票的所有歷史行情（依日期排序，方便畫走勢圖）
    List<StockPrice> findByStockCodeOrderByTradeDateAsc(String stockCode);

    // 查某檔股票、某一天的行情
    // 這個方法之後會被 StockPriceService 用來查「前一個交易日」的資料，計算漲跌幅
    Optional<StockPrice> findByStockCodeAndTradeDate(String stockCode, LocalDate tradeDate);
}
