package com.kidchang.lingopress.videoTranscriptions.dto;

public record VideoTranscriptionsRequest(
        String language,
        String videoUrl
) {

}
