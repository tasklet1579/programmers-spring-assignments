package com.github.prgrms.orders.domain;

import com.github.prgrms.orders.constant.OrderState;
import com.github.prgrms.orders.dto.OrderRequest;

import java.time.LocalDateTime;

public class Order {
    private Long seq;
    private Long userId;
    private Long productId;
    private Review review;
    private String state;
    private String requestMessage;
    private String rejectMessage;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime createAt;

    public boolean accept() {
        if (state.equals("REQUESTED")) {
            state = OrderState.ACCEPTED.getState();
            return true;
        }
        return false;
    }

    public boolean reject(OrderRequest request) {
        if (request == null || request.getMessage() == null) {
            throw new IllegalArgumentException();
        }
        if (state.equals("REQUESTED")) {
            state = OrderState.REJECTED.getState();
            requestMessage = request.getMessage();
            rejectedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean shipping() {
        if (state.equals("ACCEPTED")) {
            state = OrderState.SHIPPING.getState();
            return true;
        }
        return false;
    }

    public boolean complete() {
        if (state.equals("SHIPPING")) {
            state = OrderState.COMPLETED.getState();
            completedAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean isCompleted() {
        return state.equals("COMPLETED");
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(LocalDateTime rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
