package com.kidchang.lingopress.user;

import com.kidchang.lingopress._base.response.DataResponseDto;
import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import com.kidchang.lingopress.user.dto.request.SigninRequest;
import com.kidchang.lingopress.user.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public DataResponseDto<JwtResponse> createUser(@RequestBody SignupRequest signupRequest) {

        return DataResponseDto.of(userService.createUser(signupRequest));
    }

    @PostMapping("/sign-in")
    public DataResponseDto<JwtResponse> signIn(@RequestBody SigninRequest signinRequest) {
        return DataResponseDto.of(userService.signIn(signinRequest));
    }

}
