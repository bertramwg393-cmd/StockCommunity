package com.stockcommunity.demo.controller;

import com.stockcommunity.demo.dto.CreateOrderRequest;
import com.stockcommunity.demo.entity.Order;
import com.stockcommunity.demo.entity.User;
import com.stockcommunity.demo.repository.UserRepository;
import com.stockcommunity.demo.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
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
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Long memberId = getCurrentMemberId();
            Order order = orderService.createOrder(
                    memberId,
                    request.getStockCode(),
                    request.getOrderType(),
                    request.getQuantity(),
                    request.getPrice()
            );
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Order>> getMyOrders() {
        Long memberId = getCurrentMemberId();
        List<Order> orders = orderService.findOrdersByMemberId(memberId);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            Long memberId = getCurrentMemberId();
            Order order = orderService.cancelOrder(orderId, memberId);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalStateException e) {
        return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}