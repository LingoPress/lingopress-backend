package com.kidchang.lingopress.videoTranscriptions.messaging.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MessageResponse(
        Long queueId,
        Long pressId,
        Boolean isSuccess
) {
}
