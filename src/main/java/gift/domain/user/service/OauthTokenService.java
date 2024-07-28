package gift.domain.user.service;

import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.OauthToken;
import gift.domain.user.repository.OauthTokenJpaRepository;
import gift.domain.user.entity.User;
import gift.exception.InvalidAuthException;
import gift.external.api.kakao.KakaoApiProvider;
import gift.external.api.kakao.dto.KakaoToken;
import org.springframework.stereotype.Component;

@Component
public class OauthTokenService {

    private final OauthTokenJpaRepository oauthTokenJpaRepository;
    private final KakaoApiProvider kakaoApiProvider;

    public OauthTokenService(OauthTokenJpaRepository oauthTokenJpaRepository,
        KakaoApiProvider kakaoApiProvider) {
        this.oauthTokenJpaRepository = oauthTokenJpaRepository;
        this.kakaoApiProvider = kakaoApiProvider;
    }

    public OauthToken getOauthToken(User user, AuthProvider provider) {
        OauthToken oauthToken = oauthTokenJpaRepository.findByUserAndProvider(user, provider)
            .orElseThrow(() -> new InvalidAuthException("error.invalid.token"));
        return renew(oauthToken);
    }

    private OauthToken renew(OauthToken oauthToken) {
        KakaoToken kakaoToken = kakaoApiProvider.renewToken(oauthToken.getRefreshToken());

        oauthToken.updateInfo(kakaoToken.accessToken(), kakaoToken.refreshToken());
        return oauthTokenJpaRepository.save(oauthToken);
    }
}