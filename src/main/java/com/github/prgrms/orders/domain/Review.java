package com.github.prgrms.orders.domain;

import com.github.prgrms.orders.dto.ReviewRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Objects;

public class Review {
    private Long seq;
    private Long userSeq;
    private Long productSeq;
    private String content;
    private LocalDateTime createAt;

    public Review() {
    }

    public Review(Long userId, Order order, ReviewRequest request) {
        validateReviewState(order);
        validateReviewContent(request);
        userSeq = userId;
        productSeq = order.getProductId();
        content = request.getContent();
        createAt = LocalDateTime.now();
    }

    private void validateReviewContent(ReviewRequest request) {
        if (request.getContent() == null) {
            throw new IllegalArgumentException();
        }
        if (request.getContent().length() > 1000) {
            throw new IllegalArgumentException();
        }
    }

    private void validateReviewState(Order order) {
        if (!order.isCompleted()) {
            throw new IllegalStateException(String.format("Could not write review for order %d because state(REQUESTED) is not allowed", order.getSeq()));
        }
        if (order.getReview() != null) {
            throw new IllegalArgumentException(String.format("Could not write review for order %d because have already written", order.getSeq()));
        }
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Long userSeq) {
        this.userSeq = userSeq;
    }

    public Long getProductSeq() {
        return productSeq;
    }

    public void setProductSeq(Long productSeq) {
        this.productSeq = productSeq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(seq, review.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("userSeq", userSeq)
                .append("productSeq", productSeq)
                .append("content", content)
                .append("createAt", createAt)
                .toString();
    }
}
