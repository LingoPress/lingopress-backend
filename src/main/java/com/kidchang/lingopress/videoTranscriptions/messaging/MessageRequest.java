package com.kidchang.lingopress.videoTranscriptions.messaging;

import lombok.Builder;

@Builder
public record MessageRequest(
        String language,
        String videoUrl,
        Long id
) {
}
