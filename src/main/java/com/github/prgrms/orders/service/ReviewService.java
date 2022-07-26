package com.github.prgrms.orders.service;

import com.github.prgrms.orders.dao.ReviewRepository;
import com.github.prgrms.orders.domain.Order;
import com.github.prgrms.orders.domain.Review;
import com.github.prgrms.orders.dto.ReviewRequest;
import com.github.prgrms.products.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final OrderService orderService;
    private final ProductService productService;
    private final ReviewRepository reviewRepository;

    public ReviewService(OrderService orderService, ProductService productService, ReviewRepository reviewRepository) {
        this.orderService = orderService;
        this.productService = productService;
        this.reviewRepository = reviewRepository;
    }

    public Review review(Long userId, Long orderId, ReviewRequest request) {
        Order order = orderService.findById(orderId, userId);

        Review saved = reviewRepository.save(new Review(userId, order, request));

        order.setReview(saved);
        orderService.update(order);
        productService.updateReviewCount(order.getProductId());

        return saved;
    }
}
