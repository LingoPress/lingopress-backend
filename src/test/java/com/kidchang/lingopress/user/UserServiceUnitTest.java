package com.kidchang.lingopress.user;

import com.kidchang.lingopress.jwt.JwtService;
import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import com.kidchang.lingopress.user.dto.request.GoogleRequest;
import com.kidchang.lingopress.user.dto.response.GoogleInfResponse;
import com.kidchang.lingopress.user.dto.response.GoogleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev") // 괄호 안에 실행 환경을 명시해준다.
class UserServiceUnitTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("계정 생성 서비스 테스트")
    void testLoginWithGoogle() {
        // Given
        String authCode = "mock_auth_code";
        String googleClientId = "mock_client_id";
        String googleClientSecret = "mock_client_secret";
        String googleRedirectUrl = "mock_redirect_url";
        final String GOOGLE_ACCESS_TOKEN_URL = "https://oauth2.googleapis.com/token";

        GoogleRequest mockRequest = GoogleRequest.builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(authCode)
                .redirectUri(googleRedirectUrl)
                .grantType("authorization_code")
                .build();

        GoogleResponse mockAccessTokenResponse = GoogleResponse.builder()
                .access_token("mock_access_token")
                .id_token("mock_id_token")
                .build();

        ResponseEntity<GoogleResponse> mockAccessTokenEntity = new ResponseEntity<>(mockAccessTokenResponse, HttpStatus.OK);

        GoogleInfResponse mockUserInfoResponse = GoogleInfResponse.builder()
                .sub("mock_sub")
                .email("mock_email")
                .name("mock_name")
                .build();

        ResponseEntity<GoogleInfResponse> mockUserInfoEntity = new ResponseEntity<>(mockUserInfoResponse, HttpStatus.OK);

        User mockUser = User.builder()
                .provider("google")
                .providerId("mock_sub")
                .nickname("mock_name")
                .role("ROLE_USER")
                .email("mock_email")
                .username("g_mock_sub")
                .build();

        JwtResponse mockJwtResponse = JwtResponse.builder()
                .accessToken("mock_access_token")
                .isNewUser(true)
                .refreshToken("mock_refresh_token")
                .build();

        given(restTemplate.postForEntity(eq(GOOGLE_ACCESS_TOKEN_URL), eq(mockRequest), eq(GoogleResponse.class)))
                .willReturn(mockAccessTokenEntity);

        given(restTemplate.getForEntity(any(String.class), eq(GoogleInfResponse.class)))
                .willReturn(mockUserInfoEntity);
        given(userRepository.findByProviderAndProviderId("google", "mock_sub"))
                .willReturn(Optional.empty());
        given(userRepository.save(any(User.class)))
                .willReturn(mockUser);
        given(jwtService.issueJwt(mockUser))
                .willReturn(mockJwtResponse);


        // When
        JwtResponse result = userService.loginWithGoogle(authCode, googleClientId, googleClientSecret, googleRedirectUrl);

        // Then
        assertEquals("mock_access_token", result.accessToken());
        assertEquals("mock_refresh_token", result.refreshToken());
    }
}