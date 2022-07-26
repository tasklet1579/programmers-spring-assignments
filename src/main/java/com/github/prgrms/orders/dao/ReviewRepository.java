package com.github.prgrms.orders.dao;

import com.github.prgrms.orders.domain.Review;

public interface ReviewRepository {
    Review save(Review review);
}
