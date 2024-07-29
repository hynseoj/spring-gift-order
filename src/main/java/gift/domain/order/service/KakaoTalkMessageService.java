package gift.domain.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.order.dto.OrderResponse;
import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.OauthToken;
import gift.domain.user.entity.User;
import gift.domain.user.service.OauthApiProvider;
import gift.domain.user.service.OauthTokenService;
import gift.external.api.kakao.dto.FeedObjectRequest;
import gift.external.api.kakao.dto.FeedObjectRequest.Button;
import gift.external.api.kakao.dto.FeedObjectRequest.Content;
import gift.external.api.kakao.dto.FeedObjectRequest.Link;
import gift.external.api.kakao.dto.KakaoToken;
import gift.external.api.kakao.dto.KakaoUserInfo;
import org.springframework.stereotype.Component;

@Component
public class KakaoTalkMessageService {

    private final OauthApiProvider<KakaoToken, KakaoUserInfo> oauthApiProvider;
    private final OauthTokenService oauthTokenService;
    private final ObjectMapper objectMapper;

    public KakaoTalkMessageService(
        OauthApiProvider<KakaoToken, KakaoUserInfo> oauthApiProvider,
        OauthTokenService oauthTokenService,
        ObjectMapper objectMapper
    ) {
        this.oauthApiProvider = oauthApiProvider;
        this.oauthTokenService = oauthTokenService;
        this.objectMapper = objectMapper;
    }

    public Integer sendMessageToMe(User user, OrderResponse orderResponse) {
        String tempLinkUrl = "http://localhost:8080/api/products/" + orderResponse.orderItems().get(0).product().id();
        FeedObjectRequest templateObject = new FeedObjectRequest(
            "feed",
            new Content(
                "주문해 주셔서 감사합니다.",
                orderResponse.orderItems().get(0).product().imageUrl(),
                orderResponse.recipientMessage(),
                new Link(tempLinkUrl)
            ),
            new Button[]{
                new Button(
                    "자세히 보기",
                    new Link(tempLinkUrl)
                )
            }
        );

        OauthToken oauthToken = oauthTokenService.getOauthToken(user, AuthProvider.KAKAO);

        try {
            String serialized = objectMapper.writeValueAsString(templateObject);
            return oauthApiProvider.sendMessageToMe(oauthToken.getAccessToken(), serialized);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
