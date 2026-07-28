package com.stockcommunity.demo.repository;

import com.stockcommunity.demo.entity.Watchlist;
import com.stockcommunity.demo.entity.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, Long> {

    List<WatchlistItem> findByWatchlistId(Long watchlistId);
}
