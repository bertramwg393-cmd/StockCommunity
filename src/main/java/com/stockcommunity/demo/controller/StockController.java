package com.stockcommunity.demo.controller;

import com.stockcommunity.demo.dto.CreateStockRequest;
import com.stockcommunity.demo.entity.Stock;
import com.stockcommunity.demo.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public  StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // 新增股票主檔資料
    @PostMapping
    public ResponseEntity<?> createStock(@RequestBody CreateStockRequest request) {
        try {
            Stock stock = stockService.createStock(
                    request.getStockCode(),
                    request.getStockName(),
                    request.getMarket(),
                    request.getIndustry()
            );
            return ResponseEntity.ok(stock);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 查詢全部股票，讓下單/自選股頁面可以下拉選單選代號
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.findAllStocks());
    }

    // 依代號查單一股票
    @GetMapping("/{stockCode}")
    public ResponseEntity<?> getStock(@PathVariable String stockCode) {
        try {
            Stock stock = stockService.findByStockCode(stockCode);
            return ResponseEntity.ok(stock);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
