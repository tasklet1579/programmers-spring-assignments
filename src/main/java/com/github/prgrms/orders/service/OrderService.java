package com.github.prgrms.orders.service;

import com.github.prgrms.configures.web.SimplePageRequest;
import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.orders.dao.OrderRepository;
import com.github.prgrms.orders.domain.Order;
import com.github.prgrms.orders.dto.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findById(Long id, Long userId) {
        return orderRepository.findById(id, userId)
                              .orElseThrow(() -> new NotFoundException("Could not found order for " + id));
    }

    public List<Order> findAll(Long userId, SimplePageRequest request) {
        return orderRepository.findAll(userId, request);
    }

    public void update(Order order) {
        orderRepository.update(order);
    }

    public boolean accept(Long id, Long userId) {
        Order order = findById(id, userId);
        boolean isAccepted = order.accept();
        if (isAccepted) {
            orderRepository.update(order);
        }
        return isAccepted;
    }

    public boolean reject(Long id, Long userId, OrderRequest request) {
        Order order = findById(id, userId);
        boolean isRejected = order.reject(request);
        if (isRejected) {
            orderRepository.update(order);
        }
        return isRejected;
    }

    public boolean shipping(Long id, Long userId) {
        Order order = findById(id, userId);
        boolean isShipped = order.shipping();
        if (isShipped) {
            orderRepository.update(order);
        }
        return isShipped;
    }

    public boolean complete(Long id, Long userId) {
        Order order = findById(id, userId);
        boolean isCompleted = order.complete();
        if (isCompleted) {
            orderRepository.update(order);
        }
        return isCompleted;
    }
}
