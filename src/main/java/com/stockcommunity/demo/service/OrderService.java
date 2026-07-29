package com.stockcommunity.demo.service;

import com.stockcommunity.demo.entity.Order;
import com.stockcommunity.demo.entity.OrderType;
import com.stockcommunity.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 建立新訂單，歸屬於指定的會員
    public Order createOrder(Long memberId,
                             String stockCode,
                             OrderType orderType) {
        Order order = new Order();
        order.setMemberId(memberId);
        order.setStockCode(stockCode);
        order.setOrderType(orderType);

        return orderRepository.save(order);
    }


}
