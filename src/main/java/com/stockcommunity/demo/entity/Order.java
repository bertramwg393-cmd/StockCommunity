package com.stockcommunity.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;  // 下單的使用者 ID ，對應 users.id，先不做 JPA 關聯，用純欄位比較好懂

    @Column(nullable = false)
    private String stockCode;  // 股票代號，例如 "2330" ，先用文字存，之後 Stocks 表建好再補正式關聯

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;  // BUY 或 SELL

    @Column(nullable = false)
    private Integer quantity;  // 委託股數

    @Column(nullable = false)
    private BigDecimal price;  // 委託價格

    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // PENDING 待處理/等待中, FILLED 已成交, CANCELLED 已取消

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if(this.status == null) {
            this.status = OrderStatus.PENDING;
        }

    }

}
