package com.stockcommunity.demo.controller;

import com.stockcommunity.demo.dto.AddWatchlistItemRequest;
import com.stockcommunity.demo.dto.CreateWatchlistRequest;
import com.stockcommunity.demo.entity.User;
import com.stockcommunity.demo.entity.Watchlist;
import com.stockcommunity.demo.entity.WatchlistItem;
import com.stockcommunity.demo.repository.UserRepository;
import com.stockcommunity.demo.service.WatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final UserRepository userRepository;

    public WatchlistController(WatchlistService watchlistService, UserRepository userRepository) {
        this.watchlistService = watchlistService;
        this.userRepository = userRepository;
    }

    private Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<?> createWatchlist(@RequestBody CreateWatchlistRequest request) {
        try {
            Long memberId = getCurrentMemberId();
            Watchlist watchlist = watchlistService.createWatchlist(memberId, request.getName());
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
            Long memberId = getCurrentMemberId();
            Watchlist watchlist = watchlistService.findWatchlistById(watchlistId);
            if (!watchlist.getMemberId().equals(memberId)) {
                return ResponseEntity.status(403).body("無權限操作此清單");
            }
            WatchlistItem item = watchlistService.addWatchlistItem(watchlistId, request.getStockCode());
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeWatchlistItem(@PathVariable Long itemId) {
        try {
            Long memberId = getCurrentMemberId();
            watchlistService.removeWatchlistItem(itemId, memberId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Watchlist>> getMyWatchlists() {
        Long memberId = getCurrentMemberId();
        List<Watchlist> watchlists = watchlistService.findWatchlistsByMemberId(memberId);
        return ResponseEntity.ok(watchlists);
    }

    @GetMapping("/{watchlistId}/items")
    public ResponseEntity<?> getWatchlistItems(@PathVariable Long watchlistId) {
        Long memberId = getCurrentMemberId();
        Watchlist watchlist = watchlistService.findWatchlistById(watchlistId);
        if (!watchlist.getMemberId().equals(memberId)) {
            return ResponseEntity.status(403).body("無權限查看此清單");
        }
        List<WatchlistItem> items = watchlistService.findWatchlistItemsByWatchlistId(watchlistId);
        return ResponseEntity.ok(items);
    }

}