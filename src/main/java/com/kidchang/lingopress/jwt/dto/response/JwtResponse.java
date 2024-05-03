package com.kidchang.lingopress.jwt.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record JwtResponse(
        @Schema(example = "엄준식")
        String nickname,
        @Schema(example = "1231231232...")
        String accessToken,
        @Schema(example = "1231231232...")
        String refreshToken,

        @Schema(example = "1231231232")
        Long accessTokenExpiresIn,
        @Schema(example = "true")
        Boolean isNewUser) {

}
