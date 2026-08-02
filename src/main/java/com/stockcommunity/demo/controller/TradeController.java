package com.stockcommunity.demo.controller;

import com.stockcommunity.demo.entity.Trade;
import com.stockcommunity.demo.entity.User;
import com.stockcommunity.demo.repository.UserRepository;
import com.stockcommunity.demo.service.TradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeService tradeService;
    private final UserRepository userRepository;

    public TradeController(TradeService tradeService, UserRepository userRepository) {
        this.tradeService = tradeService;
        this.userRepository = userRepository;
    }

    // 跟 OrderController、WatchlistController 同一套邏輯：從 SecurityContext 解出 username 再查 memberId
    private Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user =userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("找不到使用者"));
        return user.getId();
    }

    // 注意：這裡故意沒有 POST 端點！
    // 成交紀錄不該讓前端直接新增，應該由撮合引擎內部呼叫 TradeService.createTrade()
    // 一旦開放 POST，使用者就能自己捏造假的成交紀錄，等於偽造交易歷史

    // 查詢目前登入者相關的所有成交紀錄（不管當初是買方還是賣方）
    // 目前只用 memberId 查 Order 太複雜，先直接依訂單串查（等 Order 增加關聯查詢時可再優化）
    @GetMapping("/mine")
    public ResponseEntity<List<Trade>> getMyTrades(@RequestParam Long orderId) {
        Long memberId = getCurrentMemberId();
        // TODO: 這裡先簡化用 orderId 查，之後應該改成先查出該使用者名下所有 Order，再逐筆查成交紀錄
        List<Trade> trades = tradeService.findTradesByOrderId(orderId);
        return ResponseEntity.ok(trades);
    }

    // 查某檔股票的所有成交紀錄，不需要驗證身份（成交資訊屬於公開行情）
    @GetMapping("/stock/{stockCode}")
    public ResponseEntity<List<Trade>> getTradesByStock(@PathVariable String stockCode) {
        List<Trade> trades =tradeService.findTradesByStockCode(stockCode);
        return ResponseEntity.ok(trades);
    }
}
