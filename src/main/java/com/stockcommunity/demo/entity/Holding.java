package com.stockcommunity.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "holdings",
        // 同一個使用者對同一檔股票只會有一筆持股紀錄，不會出現兩筆
        // 資料庫層級強制擋下重複組合，即使程式邏輯之後不小心寫錯也不會產生髒資料
        uniqueConstraints = @UniqueConstraint(columnNames = {"memberId", "stockCode"})
)
@Data
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;  // 對應 users.id

    @Column(nullable = false)
    private String stockCode;  // 股票代號

    @Column(nullable = false)
    private Integer quantity;  // 目前持有股數

    @Column(nullable = false)
    private BigDecimal averagePrice;  // 平均成本價

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
