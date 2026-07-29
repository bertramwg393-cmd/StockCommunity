package com.stockcommunity.demo.service;

import com.stockcommunity.demo.entity.Order;
import com.stockcommunity.demo.entity.OrderType;
import com.stockcommunity.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 建立新訂單，歸屬於指定的會員
    public Order createOrder(Long memberId,
                             String stockCode,
                             OrderType orderType,
                             Integer quantity,
                             BigDecimal price) {
        Order order = new Order();
        order.setMemberId(memberId);
        order.setStockCode(stockCode);
        order.setOrderType(orderType);
        order.setQuantity(quantity);
        order.setPrice(price);

        return orderRepository.save(order);
    }


    public List<Order> findOrdersByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }
}
