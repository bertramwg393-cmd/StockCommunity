package com.stockcommunity.demo.dto;

import com.stockcommunity.demo.entity.OrderType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {
    private String stockCode;
    private OrderType orderType;
    private Integer quantity;
    private BigDecimal price;
}