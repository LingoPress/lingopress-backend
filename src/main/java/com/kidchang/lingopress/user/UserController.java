package com.kidchang.lingopress.user;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress.jwt.dto.request.JwtRequest;
import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import com.kidchang.lingopress.user.dto.request.SigninRequest;
import com.kidchang.lingopress.user.dto.request.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/status")
    public String status(@Value("${greeting.message}") String message) {
        return message;
    }

    @GetMapping("/error-occur")
    public String errorOccur() {
        throw new GeneralException(Code.ERROR_OCCURRED_TEST);
    }

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public DataResponseDto<JwtResponse> createUser(@RequestBody SignupRequest signupRequest) {

        return DataResponseDto.of(userService.createUser(signupRequest));
    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public DataResponseDto<JwtResponse> signIn(@RequestBody SigninRequest signinRequest) {
        return DataResponseDto.of(userService.signIn(signinRequest));
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    public DataResponseDto<JwtResponse> reissue(@RequestBody JwtRequest jwtRequest) {
        return DataResponseDto.of(userService.reissue(jwtRequest));
    }

}
