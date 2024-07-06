package com.kidchang.lingopress.videoTranscriptions.dto;

import com.kidchang.lingopress.videoTranscriptions.VideoProcessingEnum;
import lombok.Builder;

@Builder
public record VideoTranscriptionsResponse(
        Long id,
        VideoProcessingEnum processingStatus
) {

}
