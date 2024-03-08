package com.kidchang.lingopress.user;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress.jwt.JwtService;
import com.kidchang.lingopress.jwt.dto.request.JwtRequest;
import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import com.kidchang.lingopress.user.dto.request.SigninRequest;
import com.kidchang.lingopress.user.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public JwtResponse createUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.username())) {
            throw new GeneralException(Code.DUPLICATED_USER);
        }

        //
        User signupRequestEntity = signupRequest.toEntity();

        // 암호화
        String encode_password = passwordEncoder.encode(signupRequestEntity.getPassword());
        signupRequestEntity.setPassword(encode_password);
        User user = userRepository.save(signupRequestEntity);
        JwtResponse jwtResponse = jwtService.issueJwt(user);
        return jwtResponse;
    }

    public JwtResponse signIn(SigninRequest signinRequest) {
        User user = userRepository.findByUsername(signinRequest.username());
        if (user == null) {
            throw new GeneralException(Code.NOT_FOUND_USER);
        }
        if (!passwordEncoder.matches(signinRequest.password(), user.getPassword())
        ) {
            throw new GeneralException(Code.INVALID_PASSWORD);
        }

        JwtResponse jwtResponse = jwtService.issueJwt(user);

        return jwtResponse;


    }

    public JwtResponse reissue(JwtRequest jwtRequest) {
        JwtResponse jwtResponse = jwtService.reissueJwt(jwtRequest);
        return jwtResponse;
    }
}
