package com.kidchang.lingopress.jwt;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress.jwt.dto.request.JwtRequest;
import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import com.kidchang.lingopress.user.User;
import com.kidchang.lingopress.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public JwtResponse issueJwt(User user) {
        JwtResponse jwtResponse = jwtTokenUtil.generateJwt(user);

        if (refreshTokenRepository.existsByUser(user)) { // 로그인
            RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
            refreshToken.update(jwtResponse.refreshToken());
        } else { // 회원가입
            RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(jwtResponse.refreshToken())
                .user(user)
                .build();
            refreshTokenRepository.save(refreshToken);
        }

        return jwtResponse;
    }

    public JwtResponse reissueJwt(JwtRequest jwtRequest) {
        if (!jwtTokenUtil.validateToken(jwtRequest.refreshToken())) {
            throw new GeneralException(Code.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = jwtTokenUtil.getAuthentication(jwtRequest.accessToken());
        Long userId = Long.parseLong(authentication.getName());

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GeneralException(Code.NOT_FOUND_USER));
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user);
        if (!refreshToken.getRefreshToken().equals(jwtRequest.refreshToken())) {
            throw new GeneralException(Code.INVALID_REFRESH_TOKEN);
        }

        JwtResponse jwtResponse = jwtTokenUtil.generateJwt(user);

        refreshToken.update(jwtResponse.refreshToken());

        return jwtResponse;
    }

}
