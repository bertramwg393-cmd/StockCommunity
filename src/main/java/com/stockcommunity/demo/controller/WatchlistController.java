package com.stockcommunity.demo.controller;

import com.stockcommunity.demo.dto.AddWatchlistItemRequest;
import com.stockcommunity.demo.dto.CreateWatchlistRequest;
import com.stockcommunity.demo.entity.Watchlist;
import com.stockcommunity.demo.entity.WatchlistItem;
import com.stockcommunity.demo.service.WatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @PostMapping
    public ResponseEntity<?> createWatchlist(@RequestBody CreateWatchlistRequest request) {
        try {
            Watchlist watchlist = watchlistService.createWatchlist(request.getMemberId(), request.getName());
            return ResponseEntity.ok(watchlist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{watchlistId}/items")
    public ResponseEntity<?> addWatchlistItem(
            @PathVariable Long watchlistId,
            @RequestBody AddWatchlistItemRequest request) {
        try {
            WatchlistItem item = watchlistService.addWatchlistItem(watchlistId, request.getStockCode());
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Watchlist>> getWatchlistsByMember(@PathVariable Long memberId) {
        List<Watchlist> watchlists = watchlistService.findWatchlistsByMemberId(memberId);
        return ResponseEntity.ok(watchlists);
    }

    @GetMapping("/{watchlistId}/items")
    public ResponseEntity<List<WatchlistItem>> getWatchlistItems(@PathVariable Long watchlistId) {
        List<WatchlistItem> items = watchlistService.findWatchlistItemsByWatchlistId(watchlistId);
        return ResponseEntity.ok(items);
    }

}