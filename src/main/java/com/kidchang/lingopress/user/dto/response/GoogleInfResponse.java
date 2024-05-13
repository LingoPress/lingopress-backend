package com.kidchang.lingopress.user.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GoogleInfResponse(
        String iss,
        String azp,
        String aud,
        String sub,
        String email,
        String email_verified,
        String at_hash,
        String name,
        String picture,
        String given_name,
        String family_name,
        String locale,
        String iat,
        String exp,
        String alg,
        String kid,
        String typ
) {
}