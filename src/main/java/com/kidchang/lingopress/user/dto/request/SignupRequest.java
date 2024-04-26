package com.kidchang.lingopress.user.dto.request;

import com.kidchang.lingopress.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record SignupRequest(
        @Schema(example = "gid_123123")
        String username,
        @Schema(example = "123123")
        String password,
        @Schema(example = "창현")
        String nickname) {

    public User toEntity() {
        return User.builder().role("ROLE_USER").username(username).nickname(nickname).password(password)
                .build();
    }

}
