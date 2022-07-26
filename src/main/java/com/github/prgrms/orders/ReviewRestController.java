package com.github.prgrms.orders;

import com.github.prgrms.orders.domain.Review;
import com.github.prgrms.orders.dto.ReviewDto;
import com.github.prgrms.orders.dto.ReviewRequest;
import com.github.prgrms.orders.service.ReviewService;
import com.github.prgrms.security.JwtAuthentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.github.prgrms.utils.ApiUtils.ApiResult;
import static com.github.prgrms.utils.ApiUtils.success;

@RestController
@RequestMapping("api/orders")
public class ReviewRestController {
    private final ReviewService reviewService;

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(path = "/{id}/review")
    public ApiResult<ReviewDto> review(@AuthenticationPrincipal JwtAuthentication authentication,
                                       @PathVariable Long id, @RequestBody ReviewRequest request) {
        Review review = reviewService.review(authentication.id, id, request);
        return success(new ReviewDto(review));
    }
}
