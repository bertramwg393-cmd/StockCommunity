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

    public List<Watchlist> findWatchlistsByMemberId(Long memberId) {
        return watchlistRepository.findByMemberId(memberId);
    }

    public List<WatchlistItem> findWatchlistItemsByWatchlistId(Long watchlistId) {
        return watchlistItemRepository.findByWatchlistId(watchlistId);
    }

    public Watchlist findWatchlistById(Long watchlistId) {
        return watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new IllegalArgumentException("Watchlist not found"));
    }

}