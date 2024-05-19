package com.kidchang.lingopress.user.dto.request;

import com.kidchang.lingopress._base.constant.LanguageEnum;

public record UserLanguageDto(
        LanguageEnum user_language,
        LanguageEnum target_language
) {
}
