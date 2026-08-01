package com.stockcommunity.demo.service;

import com.stockcommunity.demo.entity.Trade;
import com.stockcommunity.demo.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    // 撮合成功後呼叫，記錄一筆成交
    public Trade createTrade(Long buyOrderId,
                             Long sellOrderId,
                             String stockCode,
                             BigDecimal price,
                             Integer quantity) {
        Trade trade = new Trade();
        trade.setBuyOrderId(buyOrderId);
        trade.setSellOrderId(sellOrderId);
        trade.setStockCode(stockCode);
        trade.setPrice(price);
        trade.setQuantity(quantity);
        return tradeRepository.save(trade);
    }

    // 查某筆訂單相關的所有成交紀錄，不管這筆單當初是買方還是賣方都查得到
    public List<Trade> findTradesByOrderId(Long orderId) {
        List<Trade> asBuyer = tradeRepository.findByBuyOrderId(orderId);
        List<Trade> asSeller = tradeRepository.findBySellOrderId(orderId);

        return Stream.concat(asBuyer.stream(), asSeller.stream())
                .collect(Collectors.toList());
    }

    // 查某檔股票的所有成交紀錄
    public List<Trade> findTradesByStockCode(String stockCode) {
        return tradeRepository.findByStockCode(stockCode);
    }
}
