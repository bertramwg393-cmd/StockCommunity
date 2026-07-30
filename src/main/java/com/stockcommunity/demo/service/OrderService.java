package com.stockcommunity.demo.service;

import com.stockcommunity.demo.entity.Order;
import com.stockcommunity.demo.entity.OrderStatus;
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

    // 取消訂單，將訂單狀態設為已取消

    public Order cancelOrder(Long orderId, Long memberId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("找不到訂單"));

        if (!order.getMemberId().equals(memberId)) {
            throw new SecurityException("無權限取消此訂單");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("只能取消待處理中的訂單，目前狀態：" + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }


}
