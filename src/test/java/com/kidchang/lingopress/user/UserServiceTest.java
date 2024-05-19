package com.kidchang.lingopress.user;

import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import com.kidchang.lingopress.user.dto.request.GoogleRequest;
import com.kidchang.lingopress.user.dto.response.GoogleInfResponse;
import com.kidchang.lingopress.user.dto.response.GoogleResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev") // 괄호 안에 실행 환경을 명시해준다.
class UserServiceTest {
    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    @DisplayName("계정 생성 통합 테스트")
    @Transactional
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


        // When
        given(restTemplate.postForEntity(eq(GOOGLE_ACCESS_TOKEN_URL), eq(mockRequest), eq(GoogleResponse.class)))
                .willReturn(mockAccessTokenEntity);

        given(restTemplate.getForEntity(any(String.class), eq(GoogleInfResponse.class)))
                .willReturn(mockUserInfoEntity);


        JwtResponse result = userService.loginWithGoogle(authCode, googleClientId, googleClientSecret, googleRedirectUrl);
        //var result = userController.loginGoogle(authCode);


        // Then
        // assertThat(result.getData().isNewUser()).isTrue();
        assertThat(result.isNewUser()).isTrue();
    }
}