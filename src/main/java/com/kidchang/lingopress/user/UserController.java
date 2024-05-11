package com.kidchang.lingopress.user;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.BusinessException;
import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress.jwt.dto.request.JwtRequest;
import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${oauth2.google.client.id}")
    private String googleClientId;
    @Value("${oauth2.google.client.secret}")
    private String googleClientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String googleRedirectUrl;

    @GetMapping("/status")
    public String status(@Value("${greeting.message}") String message) {
        return message;
    }

    @GetMapping("/error-occur")
    public String errorOccur() {
        throw new BusinessException(Code.ERROR_OCCURRED_TEST);
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    public DataResponseDto<JwtResponse> reissue(@RequestBody JwtRequest jwtRequest) {
        return DataResponseDto.of(userService.reissue(jwtRequest));
    }

    @Operation(summary = "구글 로그인 페이지로 이동")
    @PostMapping("/oauth2/google")
    public DataResponseDto<String> loginUrlGoogle(@Value("${oauth2.google.redirect-uri}") String redirectUrl
    ) {
        return DataResponseDto.of("https://accounts.google.com/o/oauth2/auth?client_id=" + googleClientId + "&redirect_uri=" + redirectUrl + "&response_type=code&scope=email%20profile%20openid&access_type=offline");
    }

    @Operation(summary = "구글 로그인/회원가입 (Oauth2)")
    @GetMapping("/oauth2/google")
    public DataResponseDto<JwtResponse> loginGoogle(@RequestParam(value = "code") String authCode) {

        return DataResponseDto.of(userService.loginWithGoogle(authCode, googleClientId, googleClientSecret, googleRedirectUrl));
    }

    @Operation(summary = "탈퇴하기")
    @PatchMapping("/delete")
    public DataResponseDto<Boolean> deleteUser() {
        return DataResponseDto.of(userService.deleteUser());
    }


}
