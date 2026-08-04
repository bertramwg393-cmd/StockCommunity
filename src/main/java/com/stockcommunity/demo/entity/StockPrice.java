package com.stockcommunity.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "stock_prices",
        // 同一檔股票同一天只會有一筆行情紀錄，資料庫層級防止重複匯入
        uniqueConstraints = @UniqueConstraint(columnNames = {"stockCode", "tradeDate"})
)
@Data
public class StockPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stockCode;  // 股票代號，對應 stocks.stockCode

    @Column(nullable = false)
    private LocalDate tradeDate;  // 交易日期（只到日期，不含時間）

    @Column(nullable = false)
    private BigDecimal openPrice;  // 開盤價

    @Column(nullable = false)
    private BigDecimal highPrice;  // 最高價

    @Column(nullable = false)
    private BigDecimal lowPrice;  // 最低價

    @Column(nullable = false)
    private BigDecimal closePrice;  // 收盤價

    @Column(nullable = false)
    private Long volume;  // 成交量（考慮到大盤指數、或某些除權息，股數可能會很大，用 Long 而不是 Integer）
}
