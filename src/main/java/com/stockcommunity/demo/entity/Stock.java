package com.stockcommunity.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "stocks")
@Data
public class Stock {

    @Id
    @Column(length = 10)
    private String stockCode;   // 股票代號，例如 "2330"，自然鍵，不用流水號 id

    @Column(nullable = false)
    private String stockName;   // 股票名稱，例如 "台積電"

    private String market;      // 上市 / 上櫃

    private String industry;    // 產業別，例如 "半導體業"

}
