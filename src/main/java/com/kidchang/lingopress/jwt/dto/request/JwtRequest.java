package com.kidchang.lingopress.jwt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record JwtRequest(
    @Schema(example = "1231231232...")
    String accessToken,
    @Schema(example = "1231231232...")
    String refreshToken
) {

}
