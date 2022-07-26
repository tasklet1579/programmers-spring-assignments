package com.github.prgrms.orders;

import com.github.prgrms.configures.web.SimplePageRequest;
import com.github.prgrms.orders.domain.Order;
import com.github.prgrms.orders.dto.OrderDto;
import com.github.prgrms.orders.dto.OrderRequest;
import com.github.prgrms.orders.service.OrderService;
import com.github.prgrms.security.JwtAuthentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.prgrms.utils.ApiUtils.ApiResult;
import static com.github.prgrms.utils.ApiUtils.success;

@RestController
@RequestMapping("api/orders")
public class OrderRestController {
    private final OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ApiResult<List<OrderDto>> findAll(@AuthenticationPrincipal JwtAuthentication authentication,
                                             SimplePageRequest request) {
        List<OrderDto> orders = orderService.findAll(authentication.id, request)
                                            .stream()
                                            .map(OrderDto::new)
                                            .collect(Collectors.toList());
        return success(orders);
    }

    @GetMapping(path = "/{id}")
    public ApiResult<OrderDto> findById(@AuthenticationPrincipal JwtAuthentication authentication,
                                        @PathVariable Long id) {
        Order order = orderService.findById(id, authentication.id);

        return success(new OrderDto(order));
    }

    @PatchMapping(path = "/{id}/accept")
    public ApiResult<Boolean> accept(@AuthenticationPrincipal JwtAuthentication authentication,
                                     @PathVariable Long id) {
        return success(orderService.accept(id, authentication.id));
    }

    @PatchMapping(path = "/{id}/reject")
    public ApiResult<Boolean> reject(@AuthenticationPrincipal JwtAuthentication authentication,
                                     @PathVariable Long id,
                                     @RequestBody(required = false) OrderRequest request) {
        return success(orderService.reject(id, authentication.id, request));
    }

    @PatchMapping(path = "/{id}/shipping")
    public ApiResult<Boolean> shipping(@AuthenticationPrincipal JwtAuthentication authentication,
                         @PathVariable Long id) {
        return success(orderService.shipping(id, authentication.id));
    }

    @PatchMapping(path = "/{id}/complete")
    public ApiResult<Boolean> complete(@AuthenticationPrincipal JwtAuthentication authentication,
                         @PathVariable Long id) {
        return success(orderService.complete(id, authentication.id));
    }
}
