package com.kidchang.lingopress.user.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GoogleResponse(
        String access_token, // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰
        String expires_in,   // Access Token의 남은 수명
        String refresh_token,    // 새 액세스 토큰을 얻는 데 사용할 수 있는 토큰
        String scope,
        String token_type,   // 반환된 토큰 유형(Bearer 고정)
        String id_token) {

}