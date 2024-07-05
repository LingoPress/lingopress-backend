package com.kidchang.lingopress.videoTranscriptions;

import com.kidchang.lingopress.videoTranscriptions.dto.VideoTranscriptionsResponse;
import com.kidchang.lingopress.videoTranscriptions.messaging.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoTranscriptionsService {
    private final VideoTranscriptionsRepository videoTranscriptionsRepository;
    private final MessageSender messageSender;

    public VideoTranscriptionsResponse requestVideoTranscription(String language, String videoUrl) {
        // 비디오 자막 생성 요청
        VideoTranscriptions videoTranscriptions = videoTranscriptionsRepository.save(VideoTranscriptions.builder()
                .language(language)
                .videoUrl(videoUrl)
                .build());

        // rabbitmq에 메시지 전송
        messageSender.sendRequest(videoTranscriptions.getId() + "," + videoUrl);

        return VideoTranscriptionsResponse.builder()
                .id(videoTranscriptions.getId())
                .processingStatus(VideoProcessingEnum.PROCESSING)
                .build();
    }
}
