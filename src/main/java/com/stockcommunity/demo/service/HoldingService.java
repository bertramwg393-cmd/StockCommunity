package com.stockcommunity.demo.service;

import com.stockcommunity.demo.entity.Holding;
import com.stockcommunity.demo.repository.HoldingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class HoldingService {

    private final HoldingRepository holdingRepository;

    public HoldingService(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    // 買進成交後呼叫：更新（或新增）這個使用者對這檔股票的持股與均價
    public Holding applyBuyTrade(
            Long memberId,
            String stockCode,
            Integer tradeQuantity,
            BigDecimal tradePrice) {
        Holding holding = holdingRepository.findByMemberIdAndStockCode(memberId, stockCode)
                .orElse(null);

        if (holding == null) {
            // 第一次持有這檔股票，直接新增一筆
            Holding newHolding = new Holding();
            newHolding.setMemberId(memberId);
            newHolding.setStockCode(stockCode);
            newHolding.setQuantity(tradeQuantity);
            newHolding.setAveragePrice(tradePrice);
            return holdingRepository.save(newHolding);
        }

        // 已經持有，計算新均價：(原成本 + 這次成本) / 新總股數

        // 步驟 1：算「原本已經花了多少錢」= 原本的均價 × 原本持有的股數
        // 例：原本均價 50 元、持有 100 股 → 原本花了 5000 元
        BigDecimal originalCost = holding.getAveragePrice()
                .multiply(BigDecimal.valueOf(holding.getQuantity()));

        // 步驟 2：算「這次新買進花了多少錢」= 這次成交價 × 這次成交股數
        // 例：這次成交價 60 元、買 50 股 → 這次花了 3000 元
        BigDecimal tradeCost = tradePrice.multiply(BigDecimal.valueOf(tradeQuantity));

        // 步驟 3：算「加總之後，總共持有幾股」= 原本股數 + 這次買進股數
        // 例：100 股 + 50 股 = 150 股
        // 這裡是 int,不是 BigDecimal,因為股數是整數,不需要處理小數。
        int newQuantity = holding.getQuantity() + tradeQuantity;

        // 步驟 4：算新均價 = (原本花的錢 + 這次花的錢) ÷ 總股數
        // 例：(5000 + 3000) ÷ 150 ≈ 53.33
        // divide() 除不盡時必須指定：小數點後留幾位（這裡是 2 位）、怎麼捨入（HALF_UP = 四捨五入）
        // 不指定的話，BigDecimal 遇到除不盡會直接拋出例外，不會自己決定怎麼處理
        BigDecimal newAveragePrice = originalCost.add(tradeCost)
                .divide(BigDecimal.valueOf(newQuantity), 2, RoundingMode.HALF_UP);

        // 步驟 5：把算好的新股數、新均價寫回這筆持股紀錄
        holding.setQuantity(newQuantity);
        holding.setAveragePrice(newAveragePrice);

        // 步驟 6：存回資料庫
        // 注意：這裡存的是 holding（原本查到、已被更新的那筆），不是新建立的物件
        // holding.id 不變，資料庫執行的是 UPDATE，不是 INSERT
        return holdingRepository.save(holding);
    }

    // 賣出成交後呼叫：扣減持股，賣到 0 股就直接刪除這筆持股紀錄
    public void applySellTrade(
            Long memberId,
            String stockCode,
            Integer tradeQuantity) {
        Holding holding = holdingRepository.findByMemberIdAndStockCode(memberId, stockCode)
                .orElseThrow(() -> new IllegalArgumentException("找不到持股紀錄，無法賣出未持有的股票"));
        // 防呆：不能賣出超過目前實際持有的股數
        if (tradeQuantity > holding.getQuantity()) {
            throw new IllegalStateException(
                    "賣出股數超過持有股數，目前持有：" + holding.getQuantity() + "，欲賣出：" + tradeQuantity);
        }

        int newQuantity = holding.getQuantity() - tradeQuantity;

        // 設計決策：賣到剩 0 股時，這裡選擇「直接刪除這筆持股紀錄」
        // 另一種做法是「保留紀錄、quantity 設成 0」，兩種都合理：
        //   - 刪除：資料庫乾淨，查詢時不會出現「持有 0 股」這種空紀錄
        //   - 保留 0：可以留下「這個人曾經持有過這檔股票」的痕跡，方便之後做交易歷史分析
        // 目前選擇刪除，之後如果需要歷史痕跡，改成 setQuantity(0) + save 即可，不用整個重寫
        if (newQuantity == 0) {
            holdingRepository.delete(holding);
            return;
        }

        // 賣出不影響均價，只需要扣減股數
        holding.setQuantity(newQuantity);
        holdingRepository.save(holding);
    }

    // 查某個使用者的所有持股
    public List<Holding> findHoldingsByMemberId(Long memberId) {
        return holdingRepository.findByMemberId(memberId);
    }
}
