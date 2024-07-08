package com.kidchang.lingopress.videoTranscriptions.messaging.dto;

import lombok.Builder;

@Builder
public record MessageRequest(
        String language,
        String videoUrl,
        Long id
) {
    @Override
    public String toString() {
        return "[language=" + this.language + ", videoUrl=" + this.videoUrl + ", id=" + this.id + "]";
    }
}
