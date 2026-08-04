package com.stockcommunity.demo.controller;

import com.stockcommunity.demo.dto.CreateStockPriceRequest;
import com.stockcommunity.demo.entity.StockPrice;
import com.stockcommunity.demo.service.StockPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stock-prices")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    public StockPriceController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    // 新增一筆每日行情
    @PostMapping
    public ResponseEntity<?> createStockPrice(@RequestBody CreateStockPriceRequest request) {
        try {
            StockPrice stockPrice = stockPriceService.createStockPrice(
                    request.getStockCode(),
                    request.getTradeDate(),
                    request.getOpenPrice(),
                    request.getHighPrice(),
                    request.getLowPrice(),
                    request.getClosePrice(),
                    request.getVolume()
            );
            return ResponseEntity.ok(stockPrice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 查某檔股票的所有歷史行情，不需要驗證身份（行情資訊屬於公開資料）
    @GetMapping("/{stockCode}")
    public ResponseEntity<List<StockPrice>> getPriceHistory(@PathVariable String stockCode) {
        List<StockPrice> history = stockPriceService.findPriceHistory(stockCode);
        return ResponseEntity.ok(history);
    }

    // 查某檔股票在某一天相對於前一天的漲跌幅
    // 範例：GET /api/stock-prices/2330/change-percent?tradeDate=2026-08-04&previousDate=2026-08-01
    @GetMapping("/{stockCode}/change-percent")
    public ResponseEntity<?> getChangePercent(
            @PathVariable String stockCode,
            @RequestParam LocalDate tradeDate,
            @RequestParam LocalDate previousDate) {
        try {
            BigDecimal changePercent = stockPriceService.calculateChangePercent(stockCode, tradeDate, previousDate);
            return ResponseEntity.ok(changePercent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}