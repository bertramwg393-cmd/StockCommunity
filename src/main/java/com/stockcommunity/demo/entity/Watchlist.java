package com.stockcommunity.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "watchlists")
@Data
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;   // 對應 users.id，先不做 JPA 關聯，用純欄位比較好懂

    @Column(nullable = false)
    private String name;     // 清單名稱，例如「長期存股」

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}