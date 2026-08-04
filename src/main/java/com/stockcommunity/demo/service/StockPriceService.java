package com.stockcommunity.demo.service;

import com.stockcommunity.demo.entity.StockPrice;
import com.stockcommunity.demo.repository.StockPriceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockPriceService {

    private final StockPriceRepository stockPriceRepository;

    public StockPriceService(StockPriceRepository stockPriceRepository) {
        this.stockPriceRepository = stockPriceRepository;
    }

    // 新增一筆每日行情
    public StockPrice createStockPrice(
            String stockCode,
            LocalDate tradeDate,
            BigDecimal openPrice,
            BigDecimal highPrice,
            BigDecimal lowPrice,
            BigDecimal closePrice,
            Long volume) {
        StockPrice stockPrice = new StockPrice();
        stockPrice.setStockCode(stockCode);
        stockPrice.setTradeDate(tradeDate);
        stockPrice.setOpenPrice(openPrice);
        stockPrice.setHighPrice(highPrice);
        stockPrice.setLowPrice(lowPrice);
        stockPrice.setClosePrice(closePrice);
        stockPrice.setVolume(volume);
        return stockPriceRepository.save(stockPrice);
    }

    // 查某檔股票的所有歷史行情（依日期排序，畫走勢圖用）
    public List<StockPrice> findPriceHistory(String stockCode) {
        return stockPriceRepository.findByStockCodeOrderByTradeDateAsc(stockCode);
    }

    // 算某檔股票在某一天的漲跌幅
    //
    // 公式：漲跌幅(%) = (今天收盤價 - 前一天收盤價) ÷ 前一天收盤價 × 100
    //
    // 舉例：前一天收盤 100 元，今天收盤 105 元
    //   change = 105 - 100 = 5（漲了 5 元，正數代表上漲，負數代表下跌）
    //   change ÷ 前一天收盤價 = 5 ÷ 100 = 0.05（漲了 5%，還沒轉成百分比格式前的比例）
    //   0.05 × 100 = 5（轉成一般看得懂的百分比數字：5%）
    //
    // 這個方法不會自己「存」漲跌幅，是每次呼叫時，即時查兩天的 StockPrice、即時算出來
    // 好處：不用擔心資料庫裡存的漲跌幅跟實際的開高低收對不上（資料只有一份、不會失真）
    //
    // 回傳 null 代表查不到前一天的資料（例如這是這檔股票最早的一筆紀錄），無法計算漲跌幅
    public BigDecimal calculateChangePercent(String stockCode, LocalDate tradeDate, LocalDate previousDate) {
        // 步驟 1：查「今天」這一筆行情，查不到代表資料本身有問題，直接丟例外
        StockPrice today = stockPriceRepository.findByStockCodeAndTradeDate(stockCode, tradeDate)
                .orElseThrow(() -> new IllegalArgumentException("找不到當日行情"));

        // 步驟 2：查「前一天」這一筆行情，查不到是合理情況（例如剛上市的股票），不當例外處理
        StockPrice previous = stockPriceRepository.findByStockCodeAndTradeDate(stockCode, previousDate)
                .orElse(null);

        // 步驟 3：如果沒有前一天的資料，沒辦法算漲跌幅，直接回傳 null 讓呼叫端自己決定怎麼顯示
        if (previous == null) {
            return null;
        }

        // 步驟 4：算出「今天收盤價 - 前一天收盤價」的價差
        BigDecimal change = today.getClosePrice().subtract(previous.getClosePrice());

        // 步驟 5：價差 ÷ 前一天收盤價，得到「比例」，再乘以 100 轉成百分比數字
        // divide() 除不盡時要指定精度（這裡故意留 4 位小數，比一般金額多留一點，因為百分比常需要更細的精度）跟捨入方式
        return change.divide(previous.getClosePrice(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}
