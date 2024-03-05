package com.kidchang.lingopress.user;

import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import com.kidchang.lingopress.user.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;

    public JwtResponse createUser(SignupRequest signupRequest) {
        User user = userRepository.save(signupRequest.toEntity());
        JwtResponse loginResponse = JwtResponse.builder().accessToken("accessToken")
            .refreshToken("re").nickname(user.getNickname()).build();
        log.info("loginResponse : {}", loginResponse);
        return loginResponse;
    }
}
