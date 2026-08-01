package com.stockcommunity.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Data
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long buyOrderId;  // 對應買方那筆 Order 的 id

    @Column(nullable = false)
    private Long sellOrderId;  // 對應賣方那筆 Order 的 id

    @Column(nullable = false)
    private String stockCode;  // 成交的股票代號

    @Column(nullable = false)
    private BigDecimal price;  // 成交價

    @Column(nullable = false)
    private Integer quantity;  // 成交量

    private LocalDateTime tradedAt;

    @PrePersist
    protected void onCreate() {
        this.tradedAt = LocalDateTime.now();
    }
}
