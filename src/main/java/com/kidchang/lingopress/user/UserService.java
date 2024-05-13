package com.kidchang.lingopress.user;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress._base.utils.SecurityUtil;
import com.kidchang.lingopress.jwt.JwtService;
import com.kidchang.lingopress.jwt.dto.request.JwtRequest;
import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import com.kidchang.lingopress.user.dto.request.GoogleRequest;
import com.kidchang.lingopress.user.dto.response.GoogleInfResponse;
import com.kidchang.lingopress.user.dto.response.GoogleResponse;
import com.kidchang.lingopress.user.vo.GoogleUserInfoVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private static final String GOOGLE_ACCESS_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USER_INFO_URL = "https://oauth2.googleapis.com/tokeninfo";

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final RestTemplate restTemplate;

    public JwtResponse reissue(JwtRequest jwtRequest) {
        return jwtService.reissueJwt(jwtRequest);
    }

    @Transactional
    public JwtResponse loginWithGoogle(String authCode, String googleClientId, String googleClientSecret, String googleRedirectUrl) {
        GoogleUserInfoVO info = getGoogleUserInfo(authCode, googleClientId, googleClientSecret, googleRedirectUrl);
        User user = findOrCreateUser(info);
        if (user.getStatus().equals(UserStatusEnum.INACTIVE)) {
            throw new BusinessException(Code.USER_IS_INACTIVE);
        }

        return jwtService.issueJwt(user);

    }

    private User findOrCreateUser(GoogleUserInfoVO info) {
        Optional<User> optionalUser = userRepository.findByProviderAndProviderId("google", info.getSub());

        return optionalUser.orElseGet(() -> {
            User newUser = createNewUser(info);
            return userRepository.save(newUser);
        });
    }

    private User createNewUser(GoogleUserInfoVO info) {
        // TODO: 나중에는 가입 시 디스코드에 알림보내도록
        // 로그는 혹시나 에러가 발생해서 회원가입을 못해도 나중에 문제 해결후 연락할 수 있도록
        log.info("create new user : {}", info);
        return User.builder()
                .provider("google")
                .providerId(info.getSub())
                .nickname(info.getName())
                .role("ROLE_USER")
                .email(info.getEmail())
                .username("g_" + info.getSub())
                .status(UserStatusEnum.ACTIVE)
                .build();
    }


    /**
     * 구글로부터 access token을 받아서 user 정보를 가져옴
     *
     * @param authCode           구글 로그인 서비스로부터 받은 code
     * @param googleClientId     구글 클라이언트 ID
     * @param googleClientSecret 구글 클라이언트 시크릿
     * @param googleRedirectUrl  리다이렉트 URL
     * @return
     */
    public GoogleUserInfoVO getGoogleUserInfo(String authCode, String googleClientId, String googleClientSecret, String googleRedirectUrl) {
        return getUserInfo(getAccessToken(authCode, googleClientId, googleClientSecret, googleRedirectUrl));
    }


    private GoogleUserInfoVO getUserInfo(String accessToken) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GOOGLE_USER_INFO_URL)
                .queryParam("id_token", accessToken);

        ResponseEntity<GoogleInfResponse> resultEntity = restTemplate.getForEntity(builder.toUriString(), GoogleInfResponse.class);
        if (!resultEntity.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(Code.FAILED_TO_GET_GOOGLE_USER_INFO);
        }

        return GoogleUserInfoVO.builder()
                .sub(resultEntity.getBody().getSub())
                .email(resultEntity.getBody().getEmail())
                .name(resultEntity.getBody().getName())
                .build();
    }

    private String getAccessToken(String authCode, String googleClientId, String googleClientSecret, String googleRedirectUrl) {
        GoogleRequest googleOAuthRequestParam = GoogleRequest.builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(authCode)
                .redirectUri(googleRedirectUrl)
                .grantType("authorization_code")
                .build();

        ResponseEntity<GoogleResponse> accessEntity = restTemplate.postForEntity(GOOGLE_ACCESS_TOKEN_URL, googleOAuthRequestParam, GoogleResponse.class);
        if (!accessEntity.getStatusCode().is2xxSuccessful()) {
            throw new BusinessException(Code.FAILED_TO_GET_GOOGLE_ACCESS_TOKEN);
        }

        return accessEntity.getBody().getId_token();
    }

    public Boolean deleteUser() {
        Optional<User> userOptional = userRepository.findById(SecurityUtil.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(UserStatusEnum.INACTIVE);
            userRepository.save(user);
            return true;
        } else {
            throw new BusinessException(Code.FAILED_TO_DELETE_USER);
        }
    }

}
