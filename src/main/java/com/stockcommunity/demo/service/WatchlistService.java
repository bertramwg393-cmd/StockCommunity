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

    public Watchlist createWatchlist(Long memberId, String name) {
        Watchlist watchlist = new Watchlist();
        watchlist.setMemberId(memberId);
        watchlist.setName(name);
        return watchlistRepository.save(watchlist);
    }

    public WatchlistItem addWatchlistItem(Long watchlistId, String stockCode) {
        WatchlistItem watchlistItem = new WatchlistItem();
        watchlistItem.setWatchlistId(watchlistId);
        watchlistItem.setStockCode(stockCode);
        return watchlistItemRepository.save(watchlistItem);
    }

    // 查某會員底下所有清單
    public List<Watchlist> findWatchlistsByMemberId(Long memberId) {
        return watchlistRepository.findByMemberId(memberId);
    }

    // 查某清單底下所有股票
    public List<WatchlistItem> findWatchlistItemsByWatchlistId(Long watchlistId) {
        return watchlistItemRepository.findByWatchlistId(watchlistId);
    }

    // 用清單 id 查一個清單
    public Watchlist findWatchlistById(Long watchlistId) {
        return watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new IllegalArgumentException("Watchlist not found"));
    }



}
