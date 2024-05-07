package com.kidchang.lingopress.user;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public JwtResponse reissue(JwtRequest jwtRequest) {
        JwtResponse jwtResponse = jwtService.reissueJwt(jwtRequest);
        return jwtResponse;
    }

    @Transactional
    public JwtResponse loginWithGoogle(String authCode, String googleClientId, String googleClientSecret, String googleRedirectUrl) {
        GoogleUserInfoVO info = getSubWithGoogle(authCode, googleClientId, googleClientSecret, googleRedirectUrl);
        User user = findOrCreateUser(info);
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
        return User.builder()
                .provider("google")
                .providerId(info.getSub())
                .nickname(info.getName())
                .role("ROLE_USER")
                .email(info.getEmail())
                .username("g_" + info.getSub())
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
    public GoogleUserInfoVO getSubWithGoogle(String authCode, String googleClientId, String googleClientSecret, String googleRedirectUrl) {
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(authCode)
                .redirectUri(googleRedirectUrl)
                .grantType("authorization_code").build();
        ResponseEntity<GoogleResponse> accessEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                googleOAuthRequestParam, GoogleResponse.class);
        String jwtToken = accessEntity.getBody().getId_token();
        Map<String, String> map = new HashMap<>();
        map.put("id_token", jwtToken);
        ResponseEntity<GoogleInfResponse> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",
                map, GoogleInfResponse.class);
        return GoogleUserInfoVO.builder()
                .sub(resultEntity.getBody().getSub())
                .email(resultEntity.getBody().getEmail())
                .name(resultEntity.getBody().getName())
                .build();
    }
}
