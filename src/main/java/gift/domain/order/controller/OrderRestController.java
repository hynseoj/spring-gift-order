package gift.domain.order.controller;

import gift.config.LoginUser;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.service.KakaoTalkMessageService;
import gift.domain.order.service.OrderService;
import gift.domain.user.entity.User;
import gift.exception.ExternalApiException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final KakaoTalkMessageService kakaoTalkMessageService;

    public OrderRestController(OrderService orderService, KakaoTalkMessageService kakaoTalkMessageService) {
        this.orderService = orderService;
        this.kakaoTalkMessageService = kakaoTalkMessageService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest orderRequest, @LoginUser User user) {
        OrderResponse orderResponse = orderService.create(orderRequest, user);

        if (kakaoTalkMessageService.sendMessageToMe(user, orderResponse) != 0) {
            throw new ExternalApiException("error.kakao.talk.message.response");
        };
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
