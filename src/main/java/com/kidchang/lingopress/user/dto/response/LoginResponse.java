package com.kidchang.lingopress.user.dto.response;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import lombok.Builder;

@Builder
public record LoginResponse(
        String nickname,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn,
        Boolean isNewUser,
        LanguageEnum userLanguage,
        LanguageEnum targetLanguage
) {
}
