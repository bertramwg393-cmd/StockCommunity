package com.stockcommunity.demo.repository;

import com.stockcommunity.demo.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Long> {

    // 查某個使用者的所有持股（顯示個人投資組合用）
    List<Holding> findByMemberId(Long memberId);

    // 查某個使用者對某檔股票的持股紀錄（成交後要更新這一筆，或判斷有沒有這筆持股）
    // 回傳 Optional，因為「還沒持有這檔股票」是正常情況，不算例外
    Optional<Holding> findByMemberIdAndStockCode(Long memberId, String stockCode);
}
