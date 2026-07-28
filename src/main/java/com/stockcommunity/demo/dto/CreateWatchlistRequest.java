package com.stockcommunity.demo.dto;

import lombok.Data;

@Data
public class CreateWatchlistRequest {
    private Long memberId;
    private String name;
}
