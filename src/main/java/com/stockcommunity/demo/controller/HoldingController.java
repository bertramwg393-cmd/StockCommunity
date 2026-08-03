package com.stockcommunity.demo.controller;

import com.stockcommunity.demo.entity.Holding;
import com.stockcommunity.demo.entity.User;
import com.stockcommunity.demo.repository.UserRepository;
import com.stockcommunity.demo.service.HoldingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/holdings")
public class HoldingController {

    private final HoldingService holdingService;
    private final UserRepository userRepository;

    public HoldingController(HoldingService holdingService, UserRepository userRepository) {
        this.holdingService = holdingService;
        this.userRepository = userRepository;
    }

    // 跟 OrderController、TradeController 同一套邏輯：從 SecurityContext 解出 username 再查 memberId
    private Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("找不到使用者"));
        return user.getId();
    }

    // 注意：這裡故意沒有 POST 端點！
    // 持股是成交後系統自動計算出來的結果，不是使用者自己填的
    // 應該由撮合引擎內部呼叫 HoldingService.applyBuyTrade() / applySellTrade()

    // 查詢目前登入者的所有持股（投資組合）
    @GetMapping("/mine")
    public ResponseEntity<List<Holding>> getMyHoldings() {
        Long memberId = getCurrentMemberId();
        List<Holding> holdings = holdingService.findHoldingsByMemberId(memberId);
        return ResponseEntity.ok(holdings);
    }
}
