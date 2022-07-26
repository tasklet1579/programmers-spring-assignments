package com.github.prgrms.orders.dao;

import com.github.prgrms.configures.web.SimplePageRequest;
import com.github.prgrms.orders.domain.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(long id, long userId);
    List<Order> findAll(long userId, SimplePageRequest request);
    void update(Order order);
}
