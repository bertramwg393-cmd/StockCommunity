package com.stockcommunity.demo.repository;

import com.stockcommunity.demo.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMemberId (Long memberId);  // 查詢某使用者的所有訂單
}
