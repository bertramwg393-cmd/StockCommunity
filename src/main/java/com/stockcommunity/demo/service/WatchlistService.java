package com.stockcommunity.demo.service;

import com.stockcommunity.demo.entity.Watchlist;
import com.stockcommunity.demo.entity.WatchlistItem;
import com.stockcommunity.demo.repository.WatchlistItemRepository;
import com.stockcommunity.demo.repository.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistItemRepository watchlistItemRepository;

    public WatchlistService(WatchlistRepository watchlistRepository,
                            WatchlistItemRepository watchlistItemRepository) {
        this.watchlistRepository = watchlistRepository;
        this.watchlistItemRepository = watchlistItemRepository;
    }

    // 建立新的自選股清單
    public Watchlist createWatchlist(Long memberId, String name) {
        Watchlist watchlist = new Watchlist();
        watchlist.setMemberId(memberId);
        watchlist.setName(name);
        return watchlistRepository.save(watchlist);
    }

    // 把一檔股票加進指定清單
    public WatchlistItem addWatchlistItem(Long watchlistId, String stockCode) {
        WatchlistItem watchlistItem = new WatchlistItem();
        watchlistItem.setWatchlistId(watchlistId);
        watchlistItem.setStockCode(stockCode);
        return watchlistItemRepository.save(watchlistItem);
    }

    // 移除清單裡的一檔股票
    // memberId 是目前登入者，用來確認這個項目所屬的清單真的是他自己的，不能刪別人的
    public void removeWatchlistItem(Long itemId, Long memberId) {
        WatchlistItem item = watchlistItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        Watchlist watchlist = watchlistRepository.findById(item.getWatchlistId())
                .orElseThrow(() -> new IllegalArgumentException("Watchlist not found"));
        if (!watchlist.getMemberId().equals(memberId)) {
            throw new SecurityException("無權限刪除此項目");
        }
        watchlistItemRepository.deleteById(itemId);
    }

    // 查某個會員底下所有的自選股清單
    public List<Watchlist> findWatchlistsByMemberId(Long memberId) {
        return watchlistRepository.findByMemberId(memberId);
    }

    // 查某個清單底下所有的股票項目
    public List<WatchlistItem> findWatchlistItemsByWatchlistId(Long watchlistId) {
        return watchlistItemRepository.findByWatchlistId(watchlistId);
    }

    // 用清單 id 查一筆清單，查不到就丟例外
    public Watchlist findWatchlistById(Long watchlistId) {
        return watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new IllegalArgumentException("Watchlist not found"));
    }

}