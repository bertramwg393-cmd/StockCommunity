package com.stockcommunity.demo.dto;

import lombok.Data;

@Data
public class CreateStockRequest {
    private String stockCode;
    private String stockName;
    private String market;
    private String industry;
}
