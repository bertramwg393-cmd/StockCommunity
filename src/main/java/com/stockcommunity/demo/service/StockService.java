package com.stockcommunity.demo.service;

import com.stockcommunity.demo.entity.Stock;
import com.stockcommunity.demo.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // 新增一檔股票到主檔（之後串真實資料時，這裡會被排程呼叫取代手動新增）
    public Stock createStock(String stockCode,
                             String stockName,
                             String market,
                             String industry) {
        Stock stock = new Stock();
        stock.setStockCode(stockCode);
        stock.setStockName(stockName);
        stock.setMarket(market);
        stock.setIndustry(industry);
        return stockRepository.save(stock);
    }

    // 查詢全部股票主檔
    public List<Stock> findAllStocks() {
        return stockRepository.findAll();
    }

    // 依代號查單一股票，找不到就丟例外
    public Stock findByStockCode(String stockCode) {
        return stockRepository.findById(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("找不到股票代號：" + stockCode));
    }
}
